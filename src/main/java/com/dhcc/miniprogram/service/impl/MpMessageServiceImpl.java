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
import com.dhcc.miniprogram.dto.DtoSubscribeMessage;
import com.dhcc.miniprogram.dto.DtoSubscribeMessageRequest;
import com.dhcc.miniprogram.dto.DtoUser;
import com.dhcc.miniprogram.enums.BusinessCodeEnum;
import com.dhcc.miniprogram.enums.MpSendMsgStatusEnum;
import com.dhcc.miniprogram.enums.SendMsgTypeEnum;
import com.dhcc.miniprogram.model.MpMessage;
import com.dhcc.miniprogram.model.MpUser;
import com.dhcc.miniprogram.service.MpMessageService;
import com.dhcc.miniprogram.service.MpUserService;
import com.dhcc.miniprogram.util.AccessTokenUtil;
import com.dhcc.miniprogram.util.CheckInParamUtil;
import com.dhcc.miniprogram.util.DateUtil;
import com.dhcc.miniprogram.util.SimpleAlgorithmUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author cb
 * @date 2020/6/28
 * description：小程序消息业务实现类
 */
@Service
public class MpMessageServiceImpl extends BaseServiceImpl<MpMessageDao, MpMessage, String> implements MpMessageService {

    private static final Logger log = LoggerFactory.getLogger(MpMessageServiceImpl.class);

    @Autowired
    private MpUserService userService;

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    /**
     * DEV ：开发模式
     */
    private static final String DEV = "dev";

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
        // 检查空值
        CheckInParamUtil.checkEmptyValue(signature,timestamp,nonce);
        // 创建生成微信加密签名数组
        String[] arr = new String[]{wechatConfig.getWechatToken(), timestamp, nonce};
        log.info("接口名称：验证消息来自微信服务器");
        log.info("接口参数：腾讯回调地址");
        log.info("接口参数：" + JSON.toJSONString(new String[]{wechatConfig.getWechatToken(), timestamp, nonce, signature}));
        // 判断微信加密签名是否相等
        if (signature.equals(SimpleAlgorithmUtil.genSignature(arr))) {
            // 返回微信需要字符串
            return echostr;
        } else {
            // 抛异常
            log.debug("验证消息来自微信服务器失败,参数数组{}", JSON.toJSON(arr));
            throw new BusinessException(BusinessCodeEnum.VERIFY_SERVER_FAIL_EXCEPTION.getMsg());
        }

    }

    @Override
    @Transactional
    public DtoBasicResult sendSubscribeMessageByPhone(DtoSubscribeMessageRequest request) {
        // 记录发送订阅消息入参
        log.info("发送订阅消息入参：" + JSON.toJSONString(request));
        // 声明基础返回结果类
        DtoBasicResult dtoBasicResult = new DtoBasicResult();
        // 检验秘钥是否合法
        checkSecret(dtoBasicResult,request.getSecret());
        // 不为空，那就是非法的
        if(dtoBasicResult.getErrcode() != null){
            return dtoBasicResult;
        }
        // 获取 accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request,dtoBasicResult, SendMsgTypeEnum.SINGLE);
        // 不为空，参数有为空
        if(dtoBasicResult.getErrcode() != null){
            return dtoBasicResult;
        }
        // 设置手机号查询条件
        String phoneNumber = request.getPhoneNumber();
        // 根据 phoneNum 查询对应用户信息
        SimpleCondition sc = new SimpleCondition().addParm("phoneNum", phoneNumber);
        MpUser user = userService.findOne(sc);
        // 判断是否有此手机号的用户
        if (user == null) {
            // 返回不存在此手机号的用户结果
            dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_SINGLE_MESSAGE_NOT_EXISTS_PHONE.getCode())
                    .setErrmsg(BusinessCodeEnum.SEND_SINGLE_MESSAGE_NOT_EXISTS_PHONE.getMsg());
            // 记录日志
            log.info("发送订阅消息失败结果：" + JSON.toJSONString(dtoBasicResult));
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
        MpMessage mpMessage = DtoSubscribeMessage.toPO(request);
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
            String reqUrl = MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl() + "?access_token=" + accessToken;
            // 发送Post请求并接收字符串结果
            String result = HttpClientUtil.doPost(httpClient, reqUrl, reqData, headerMap);
            log.info("发送订阅消息结果：" + result);
            // 解析字符串并转为 DtoBasicResult 对象
            dtoBasicResult = JSON.parseObject(result, DtoBasicResult.class);
            // errCode是 null | 0L 为成功，否则为失败
            if (dtoBasicResult.getErrcode() == null || BusinessCodeEnum.RECEIVE_SUCCESS.getCode().equals(dtoBasicResult.getErrcode())) {
                // mpMessage 发送状态：成功标识
                mpMessage.setSendStatus(MpSendMsgStatusEnum.CG.getCode());
                // 请求成功
                dtoBasicResult.setErrcode(BusinessCodeEnum.REQUEST_SUCCESS.getCode()).setErrmsg(BusinessCodeEnum.REQUEST_SUCCESS.getMsg());
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
            log.debug(BusinessCodeEnum.SEND_SINGLE_MESSAGE_EXCEPTION.getMsg(), e);
            return dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_SINGLE_MESSAGE_EXCEPTION.getCode()).setErrmsg(BusinessCodeEnum.SEND_SINGLE_MESSAGE_EXCEPTION.getMsg());
        }
    }

    @Override
    public DtoBasicResult sendMassMessageByPhoneList(DtoSubscribeMessageRequest request) {
        // 打印入参日志
        log.info("群发消息入参", JSON.toJSONString(request));
        // 声明基础返回结果类
        DtoBasicResult dtoBasicResult = new DtoBasicResult();
        // 检验秘钥是否合法
        checkSecret(dtoBasicResult,request.getSecret());
        // 不为空，那就是非法的
        if(dtoBasicResult.getErrcode() != null){
            return dtoBasicResult;
        }
        // 获取 accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request,dtoBasicResult, SendMsgTypeEnum.MASS);
        // 不为空，参数有为空
        if(dtoBasicResult.getErrcode() != null){
            return dtoBasicResult;
        }
        // 发送失败的手机号列表
        LinkedList<String> sendFailPhoneList = new LinkedList<>();
        // 发送成功的手机号列表
        LinkedList<String> sendSuccessPhoneList = new LinkedList<>();
        // 获取手机号集合
        List<String> numberList = request.getPhoneNumberList();
        // 根据手机号集合查询用户集合
        String appId = wechatConfig.getAppId();
        List<DtoUser> userList = userService.getDtoUserList(numberList, appId);
        // 打印用户信息日志
        log.info("群发消息用户列表长度：" + userList.size());
        log.info("群发消息用户列表：" + JSON.toJSONString(userList));
        // 用户列表为空，发送订阅消息结果
        if (CollectionUtils.isEmpty(userList)) {
            return dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_MASS_MESSAGE_NOT_EXISTS_PHONE.getCode())
                    .setErrmsg(BusinessCodeEnum.SEND_MASS_MESSAGE_NOT_EXISTS_PHONE.getMsg());
        }
        // 打印日志
        log.info("接口名称：" + MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getName());
        log.info("接口参数：" + MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl());
        log.info("接口路径参数：access_token=" + accessToken);
        // 用户迭代器
        Iterator<DtoUser> userIterator = userList.iterator();
        while (userIterator.hasNext()) {
            // 获取用户OpenId 赋值给请求体的toUser属性
            DtoUser user = userIterator.next();
            request.setTouser(user.getOpenId());
            // 获取用户手机号
            String phone = user.getPhoneNum();
            // 获取httpClient
            CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
            // 将对象转为Json字符串
            String reqData = JSON.toJSONString(request);
            // 设置请求体参数
            HashMap<String, String> headerMap = new HashMap<>(1);
            // DtoSubscribeMessageRequest 转 MpMessage
            // 默认 sendStatus 是 CLZ-处理中
            MpMessage mpMessage = DtoSubscribeMessage.toPO(request);
            // 给小程序ID赋值并添加发送消息
            mpMessage.setAppId(appId);
            dao.save(mpMessage);
            // 记录日志
            log.info("接口JSON参数：" + reqData);
            // 发送 http-post 请求
            try {
                // 设置 Url 带参
                String reqUrl = MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl() + "?access_token=" + accessToken;
                // 发送Post请求并接收字符串结果
                String result = HttpClientUtil.doPost(httpClient, reqUrl, reqData, headerMap);
                log.info("群发消息单条结果：手机号=" + phone + ",返回结果=" + result);
                // 解析字符串并转为 DtoBasicResult 对象
                dtoBasicResult = JSON.parseObject(result, DtoBasicResult.class);
                // errCode是 null | 0L 为成功，否则为失败
                if (dtoBasicResult.getErrcode() == null || BusinessCodeEnum.RECEIVE_SUCCESS.getCode().equals(dtoBasicResult.getErrcode())) {
                    // 1、mpMessage 发送状态：成功标识
                    mpMessage.setSendStatus(MpSendMsgStatusEnum.CG.getCode());
                    // 2、记录添加成功手机号
                    sendSuccessPhoneList.add(phone);
                } else {
                    // 1、mpMessage 发送状态：失败
                    mpMessage.setSendStatus(MpSendMsgStatusEnum.SB.getCode());
                    // 2、记录失败手机号
                    sendFailPhoneList.add(phone);
                }
                // 记录发送时间并修改对象
                mpMessage.setSendTmplTime(DateUtil.getCurrentDate());
                dao.update(mpMessage);
            } catch (Exception e) {
                // 记录日志和抛异常
                log.debug("群发消息异常", e);
                // 添加到发送失败手机号列表
                sendFailPhoneList.add(phone);
            }
        }
        /**
         * 消息发送情况分析
         * sendSuccessPhoneList为null,消息发送全部失败
         * sendFailPhoneList为null、 sendSuccessPhoneList.size() 与 userList.size() 相等，消息发送全部成功
         * 否则，消息发送部分成功，部分失败
         */
        if (CollectionUtils.isEmpty(sendSuccessPhoneList)) {
            dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_MASS_MESSAGE_ALL_FAIL.getCode()).setErrmsg(BusinessCodeEnum.SEND_MASS_MESSAGE_ALL_FAIL.getMsg());
        } else if (CollectionUtils.isEmpty(sendFailPhoneList) && sendSuccessPhoneList.size() == userList.size()) {
            dtoBasicResult.setErrcode(BusinessCodeEnum.REQUEST_SUCCESS.getCode()).setErrmsg(BusinessCodeEnum.REQUEST_SUCCESS.getMsg());
        } else {
            dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_MASS_MESSAGE_PRAT_FAIL.getCode())
                    .setErrmsg(String.format(BusinessCodeEnum.SEND_MASS_MESSAGE_PRAT_FAIL.getMsg(),sendSuccessPhoneList,sendFailPhoneList));
        }
        // 打印群发结果
        log.info("群发消息结果", JSON.toJSONString(dtoBasicResult));
        return dtoBasicResult;
    }

    /**
     * 检查秘钥
     * @param basicResult 基础结果
     * @param secret 秘钥
     * @return 基础结果类
     */
    private void checkSecret(DtoBasicResult basicResult,String secret){
        // 判断秘钥是否为空
        if (StringUtils.isEmpty(secret)) {
            // 为空，抛没有权限
            basicResult.setErrcode(BusinessCodeEnum.AUTH_NOT_EXISTS_SECRET.getCode()).setErrmsg(BusinessCodeEnum.AUTH_NOT_EXISTS_SECRET.getMsg());
        } else {
            // 秘钥不为空，判断模式
            if(wechatConfig.getMode().equals(DEV)){
                // 正式模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getTestSecretList().contains(secret)){
                    basicResult.setErrcode(BusinessCodeEnum.AUTH_ERROR_SECRET.getCode()).setErrmsg(BusinessCodeEnum.AUTH_ERROR_SECRET.getMsg());
                }
            } else {
                // 测试模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getTestSecretList().contains(secret)){
                    basicResult.setErrcode(BusinessCodeEnum.AUTH_ERROR_SECRET.getCode()).setErrmsg(BusinessCodeEnum.AUTH_ERROR_SECRET.getMsg());
                }
            }
        }
    }

}
