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
import com.dhcc.miniprogram.dto.DtoUser;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

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
     * MP_RECEIVE_SUCCESS ：成功标识
     */
    private static final Integer MP_RECEIVE_SUCCESS = 0;

    /**
     * RESPONSE_SEND_SUCCESS ：发送成功
     * RESPONSE_SEND_FAIL ：发送失败
     * RESPONSE_NOT_EXISTS_USER ：用户不存在
     */
    private static final Integer RESPONSE_SEND_SUCCESS = 200;
    private static final Integer RESPONSE_SEND_PRAT_FAIL = 1041;
    private static final Integer RESPONSE_SEND_ALL_FAIL = 1042;
    private static final Integer RESPONSE_NOT_EXISTS_USER = 1049;

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
            log.debug("验证消息来自微信服务器抛异常,参数数组{}", JSON.toJSON(arr));
            throw new BusinessException("验证消息来自微信服务器抛异常");
        }

    }

    @Override
    @Transactional
    public DtoBasicResult sendSubscribeMessageByPhone(DtoSubscribeMessageRequest request) {
        // 记录发送订阅消息入参
        log.info("发送订阅消息入参：" + JSON.toJSONString(request));
        // 声明基础返回结果类
        DtoBasicResult dtoBasicResult;
        // 获取 accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request, SendMsgTypeEnum.SINGLE);
        // 设置手机号查询条件
        String phoneNumber = request.getPhoneNumber();
        // 根据 phoneNum 查询对应用户信息
        SimpleCondition sc = new SimpleCondition().addParm("phoneNum", phoneNumber);
        MpUser user = userService.findOne(sc);
        // 判断是否有此手机号的用户
        if (user == null) {
            // 返回不存在此手机号的用户结果
            dtoBasicResult = new DtoBasicResult(RESPONSE_NOT_EXISTS_USER, "该手机号不存在");
            // 记录日志
            log.info("发送订阅消息失败结果：" + JSON.toJSONString(dtoBasicResult));
            return dtoBasicResult;
        } else {
            // 手机号不为空、订阅消息绑定OpenId
            request.setTouser(user.getOpenId());
        }
        // 获取httpClient
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        // 手机号置空
        request.setPhoneNumber(null);
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
            String reqUrl = MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl() + "?access_token=" + accessToken;
            // 发送Post请求并接收字符串结果
            String result = HttpClientUtil.doPost(httpClient, reqUrl, reqData, headerMap);
            log.info("发送订阅消息结果：" + result);
            // 解析字符串并转为 DtoBasicResult 对象
            dtoBasicResult = JSON.parseObject(result, DtoBasicResult.class);
            // errCode是 null | 0L 为成功，否则为失败
            if (dtoBasicResult.getErrcode() == null || dtoBasicResult.getErrcode().equals(MP_RECEIVE_SUCCESS)) {
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
            log.debug("发送订阅消息异常", e);
            throw new BusinessException("发送订阅消息异常");
        }
    }

    @Override
    public DtoBasicResult sendMassMessageByPhoneList(DtoSubscribeMessageRequest request) {
        /**
         * request.setPhoneNumberList(null);
         * StopWatch
         */
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("测试小程序群发消息耗时");

        // 打印入参日志
        log.info("群发消息入参", JSON.toJSONString(request));
        // 声明基础返回结果类
        DtoBasicResult dtoBasicResult;
        // 获取 accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request, SendMsgTypeEnum.MASS);
        // 发送失败的手机号列表
        LinkedList<String> sendFailPhoneList = new LinkedList<>();
        // 发送失败的手机号列表
        LinkedList<String> sendSuccessPhoneList = new LinkedList<>();
        // 不存在的手机号列表
        LinkedList<String> notExistsPhoneList = new LinkedList<>();
        // 获取手机号集合
        List<String> numberList = request.getPhoneNumberList();
        // 根据手机号集合查询用户集合
        String appId = wechatConfig.getAppId();
        List<DtoUser> userList = userService.getDtoUserList(numberList, appId);
        // 打印用户信息日志
        log.info("群发消息用户列表长度：" + userList.size());
        log.info("群发消息用户列表：" + JSON.toJSONString(userList));
        // 用户列表不为空，发送订阅消息
        if (CollectionUtils.isNotEmpty(userList)) {
            // 设置手机号列表为空
//            request.setPhoneNumberList(null);

            for (int i = 0; i < request.getCount(); i++) {
                userList.addAll(userList);
            }

            // 用户迭代器
            Iterator<DtoUser> userIterator = userList.iterator();
            while (userIterator.hasNext()) {
                // 获取用户OpenId 赋值给请求体的toUser属性
                DtoUser user = userIterator.next();
                request.setTouser(user.getOpenId());
                // 获取用户手机号
                String phone = user.getPhoneNum();
                //
                if (numberList.contains(phone)) {
                    notExistsPhoneList.remove(phone);
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
                mpMessage.setAppId(appId);
                dao.save(mpMessage);
                // 记录日志
                log.info("接口名称：" + MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getName());
                log.info("接口参数：" + MiniproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl());
                log.info("接口路径参数：access_token=" + accessToken);
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
                    if (dtoBasicResult.getErrcode() == null || dtoBasicResult.getErrcode().equals(MP_RECEIVE_SUCCESS)) {
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
                    sendFailPhoneList.add(phone);
                    // 记录日志和抛异常
                    log.info("群发消息发送成功手机号：" + sendSuccessPhoneList + "，失败手机号：" + sendFailPhoneList);
                    log.debug("群发消息异常", e);
                }
            }
            //
//            notExistsPhoneList.addAll()
            /**
             * 消息发送情况分析
             * sendSuccessPhoneList为null,消息发送全部失败
             * sendFailPhoneList为null、 sendSuccessPhoneList.size() 与 userList.size() 相等，消息发送全部成功
             * 否则，消息发送部分成功，部分失败
             */
            if (CollectionUtils.isEmpty(sendSuccessPhoneList)) {
                dtoBasicResult = new DtoBasicResult(RESPONSE_SEND_ALL_FAIL, "消息发送全部失败");
            } else if (CollectionUtils.isEmpty(sendFailPhoneList) && sendSuccessPhoneList.size() == userList.size()) {
                dtoBasicResult = new DtoBasicResult(RESPONSE_SEND_SUCCESS, "消息发送全部成功");
            } else {
                dtoBasicResult = new DtoBasicResult(RESPONSE_SEND_PRAT_FAIL, "消息发送成功手机号：" + sendSuccessPhoneList + "，失败手机号：" + sendFailPhoneList);
            }
        } else {
            dtoBasicResult = new DtoBasicResult(RESPONSE_NOT_EXISTS_USER, "消息发送手机号用户不存在：" + numberList);
        }

        // 打印群发结果
        log.info("群发消息结果", JSON.toJSONString(dtoBasicResult));


        stopWatch.stop();
        double timeSeconds = stopWatch.getTotalTimeSeconds();
        log.info("cost:" + timeSeconds);
        log.info(stopWatch.prettyPrint());


        return dtoBasicResult;
    }

}
