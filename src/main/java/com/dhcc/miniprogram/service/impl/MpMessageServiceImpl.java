package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.dao.query.SimpleCondition;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.miniprogram.config.MiniproUrlConfig;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dao.MpMessageDao;
import com.dhcc.miniprogram.dto.DtoBasicResult;
import com.dhcc.miniprogram.dto.DtoSubscribeMessageRequest;
import com.dhcc.miniprogram.enums.MpSendMsgStatusEnum;
import com.dhcc.miniprogram.model.MpMessage;
import com.dhcc.miniprogram.model.MpUser;
import com.dhcc.miniprogram.service.MpMessageService;
import com.dhcc.miniprogram.service.MpUserService;
import com.dhcc.miniprogram.util.AccessTokenUtil;
import com.dhcc.miniprogram.util.CheckInParamUtil;
import com.dhcc.miniprogram.util.DateUtil;
import com.dhcc.miniprogram.util.SimpleAlgorithmUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author cb
 * @date 2020/6/28
 * description：小程序消息业务实现类
 */
@Service
public class MpMessageServiceImpl extends BaseServiceImpl<MpMessageDao, MpMessage,String> implements MpMessageService {

    private static final Logger log = LoggerFactory.getLogger(MpMessageServiceImpl.class);

    @Autowired
    private MpUserService userService;

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    /**
     * MP_RECEIVE_SUCCESS ：成功标识
     */
    private static final Integer MP_RECEIVE_SUCCESS = 0;

    /**
     * RESPONSE_NOT_EXISTS_USER ：用户不存在
     */
    private static final Integer RESPONSE_NOT_EXISTS_USER = 1041;

    @Override
    public String verifyMsgFromWechat(HttpServletRequest request) {
        /**
         * signature 微信加密签名
         * timestamp 时间戳
         * nonce 随机数
         * echostr 随机字符串
         */
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        // 创建生成微信加密签名数组
        String[] arr = new String[]{ wechatConfig.getWechatToken(), timestamp, nonce };
        log.info("接口名称：验证消息的确来自微信服务器");
        log.info("接口参数：腾讯回调地址");
        log.info("接口参数：" + JSON.toJSONString(new String[]{ wechatConfig.getWechatToken(), timestamp, nonce ,signature }));
        // 判断微信加密签名是否相等
        if (signature.equals(SimpleAlgorithmUtil.genSignature(arr))) {
            // 返回微信需要字符串
            return echostr;
        } else {
            // 抛异常
            log.debug("验证消息的确来自微信服务器失败,参数数组{}",JSON.toJSON(arr));
            throw new BusinessException("验证消息的确来自微信服务器失败");
        }

    }

    @Override
    public DtoBasicResult sendSubscribeMessageByPhone(DtoSubscribeMessageRequest request) {
        // 记录小程序发送订阅消息入参
        log.info("小程序发送订阅消息入参："+JSON.toJSONString(request));
        // 声明基础返回结果类
        DtoBasicResult dtoBasicResult;
        // 获取 accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request);
        // 设置手机号查询条件
        String phoneNumber = request.getPhoneNumber();
        // 根据 phoneNum 查询对应用户信息
        SimpleCondition sc = new SimpleCondition().addParm("phoneNum", phoneNumber);
        MpUser user = userService.findOne(sc);
        // 判断是否有此手机号的用户
        if(user == null){
            // 返回不存在此手机号的用户结果
            dtoBasicResult = new DtoBasicResult(RESPONSE_NOT_EXISTS_USER, "该手机号不存在");
            // 记录日志
            log.info("小程序发送订阅消息失败结果：" + JSON.toJSONString(dtoBasicResult));
            return dtoBasicResult;
        } else {
            // 手机号不为空、订阅消息绑定OpenId
            request.setTouser(user.getOpenId());
        }
        // 获取httpClient
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        // 将对象转为Json字符串
        String reqData = JSON.toJSONString(request);
        // 设置请求体参数
        HashMap<String, String> headerMap = new HashMap<>(1);
        // DtoSubscribeMessageRequest 转 MpMessage
        // 默认 sendStatus 是 CLZ-处理中
        MpMessage mpMessage = DtoSubscribeMessageRequest.toPO(request);
        // 给小程序ID赋值并添加发送消息
        mpMessage.setAppId(wechatConfig.getAppId());
        dao.save(mpMessage);
        // 发送 http-post 请求
        try {
            log.info("接口名称：" + MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getName());
            log.info("接口参数：" + MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl());
            log.info("接口路径参数：access_token=" + accessToken);
            log.info("接口JSON参数：" + reqData);
            // 设置 Url 带参
            String reqUrl = MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl() + "?access_token=" + accessToken ;
            // 发送Post请求并接收字符串结果
            String result = HttpClientUtil.doPost(httpClient, reqUrl, reqData, headerMap);
            log.info("小程序发送订阅消息结果："+ result);
            // 解析字符串并转为 DtoBasicResult 对象
            dtoBasicResult = JSON.parseObject(result, DtoBasicResult.class);
            // errCode是 null | 0L 为成功，否则为失败
            if(dtoBasicResult.getErrcode() == null || dtoBasicResult.getErrcode().equals(MP_RECEIVE_SUCCESS)){
                // mpMessage 发送状态：成功标识
                mpMessage.setSendStatus(MpSendMsgStatusEnum.CG.getCode());
            } else {
                // mpMessage 发送状态：失败
                mpMessage.setSendStatus(MpSendMsgStatusEnum.SB.getCode());
            }
            // 记录发送时间并修改对象
            mpMessage.setSendTmplTime(DateUtil.getCurrentDate());
            dao.update(mpMessage);
            // 返回对象
            return dtoBasicResult;
        } catch (Exception e) {
            // 记录日志和抛异常
            log.debug("小程序发送订阅消息异常", e);
            throw new BusinessException("小程序发送订阅消息异常");
        }
    }
}
