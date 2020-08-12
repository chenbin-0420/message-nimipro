package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dao.MpMessageDao;
import com.dhcc.miniprogram.dto.DtoBasicResult;
import com.dhcc.miniprogram.dto.DtoSubscribeMessage;
import com.dhcc.miniprogram.dto.DtoSubscribeMessageRequest;
import com.dhcc.miniprogram.dto.DtoUser;
import com.dhcc.miniprogram.enums.BusinessCodeEnum;
import com.dhcc.miniprogram.enums.MpSendMsgStatusEnum;
import com.dhcc.miniprogram.enums.SendMsgTypeEnum;
import com.dhcc.miniprogram.model.MpMassJournal;
import com.dhcc.miniprogram.model.MpMessage;
import com.dhcc.miniprogram.service.MpMassJournalService;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    @Autowired
    private MpMassJournalService massJournalService;

    @Lazy
    @Autowired
    private MpMessageService self_;


    /**
     * ERROR_MARK : 错误标记
     * ACCESS_TOKEN_INVALID : access_token 失效
     * ACCESS_TOKEN_EXPIRED ：access_token 过期编码
     * ACCESS_TOKEN_MARK ：access_token 标记
     */
    private static final String ERROR_MARK = "<html>";
    private static final int ACCESS_TOKEN_INVALID = 40001;
    private static final int ACCESS_TOKEN_EXPIRED = 42001;
    private static final String ACCESS_TOKEN_MARK = "access_token";

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
        log.info(String.format("接口参数：%s", JSON.toJSONString(new String[]{wechatConfig.getWechatToken(), timestamp, nonce, signature})));
        // 判断微信加密签名是否相等
        if (signature.equals(SimpleAlgorithmUtil.genSignature(arr))) {
            // 返回微信需要字符串
            return echostr;
        } else {
            // 抛异常
            log.error("验证消息来自微信服务器失败,参数数组{}", JSON.toJSON(arr));
            throw new BusinessException(BusinessCodeEnum.VERIFY_SERVER_FAIL_EXCEPTION.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DtoBasicResult sendSubscribeMessageByPhone(DtoSubscribeMessageRequest request) {
        // 记录发送订阅消息入参
        log.info("发送订阅消息入参：" + JSON.toJSONString(request));
        // 声明基础返回结果类
        DtoBasicResult dtoBasicResult = new DtoBasicResult();
        // 检验秘钥是否合法
        CheckInParamUtil.checkSecret(dtoBasicResult,wechatConfig,request.getSecret());
        // 不为空，那就是非法的
        if(dtoBasicResult.getErrcode() != null){
            log.error("发送订阅消息："+JSON.toJSONString(dtoBasicResult));
            return dtoBasicResult;
        }
        // 获取 accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request,dtoBasicResult, SendMsgTypeEnum.SINGLE);
        // 不为空，参数有为空
        if(dtoBasicResult.getErrcode() != null){
            log.error("发送订阅消息："+JSON.toJSONString(dtoBasicResult));
            return dtoBasicResult;
        }
        // 根据 phoneNum 查询对应openid
        String phoneNumber = request.getPhoneNumber();
        Object openId = userService.getOpenIdByPhone(phoneNumber);
        // 判断是否有此手机号的用户
        if (openId == null) {
            // 返回不存在此手机号的用户结果
            dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_SINGLE_MESSAGE_NOT_EXISTS_PHONE.getCode())
                    .setErrmsg(BusinessCodeEnum.SEND_SINGLE_MESSAGE_NOT_EXISTS_PHONE.getMsg());
            // 记录日志
            log.error("发送订阅消息失败结果：" + JSON.toJSONString(dtoBasicResult)+",手机号："+ phoneNumber);
            return dtoBasicResult;
        } else {
            // 手机号不为空、订阅消息绑定OpenId
            request.setTouser(openId.toString());
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
        // 小程序ID赋值,发送类型：单条消息类型并添加发送消息
        mpMessage.setAppId(wechatConfig.getAppId());
        mpMessage.setSendType(SendMsgTypeEnum.SINGLE.getCode());
        // 发送 http-post 请求
        try {
            // 发送订阅消息接口Url
            String sendSubscribeUrl = wechatConfig.getSendSubscribeUrl();
            log.info("接口名称：小程序发送订阅消息");
            log.info(String.format("接口参数：%s", sendSubscribeUrl));
            log.info(String.format("接口路径参数：access_token=%s",accessToken));
            log.info(String.format("接口JSON参数：%s",reqData));
            // 设置 Url 带参
            String reqUrl = sendSubscribeUrl + "?access_token=" + accessToken;
            // 发送Post请求并接收字符串结果
            String result = HttpClientUtil.doPost(httpClient, reqUrl, reqData, headerMap);
            log.info("发送订阅消息结果：" + result);
            // result 中包含<html>，那是腾讯接口服务器错误500
            if(StringUtils.contains(result,ERROR_MARK)){
                dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_SINGLE_MESSAGE_SERVER.getCode()).setErrmsg(BusinessCodeEnum.SEND_SINGLE_MESSAGE_SERVER.getMsg());
                // 记录发送时间并添加对象，并记录结果编码、结果消息
                mpMessage.setSendTmplTime(DateUtil.getCurrentDate());
                mpMessage.setErrcode(dtoBasicResult.getErrcode());
                mpMessage.setErrmsg(dtoBasicResult.getErrmsg());
                dao.save(mpMessage);
                log.error("发送订阅消息："+JSON.toJSONString(dtoBasicResult)+",手机号："+ phoneNumber);
                return dtoBasicResult;
            }
            // 解析字符串并转为 DtoBasicResult 对象
            dtoBasicResult = JSON.parseObject(result, DtoBasicResult.class);
            // null | 0L 为成功，否则为失败
            if (dtoBasicResult.getErrcode() == null || BusinessCodeEnum.RECEIVE_SUCCESS.getCode().equals(dtoBasicResult.getErrcode())) {
                // mpMessage 发送状态：成功标识
                mpMessage.setSendStatus(MpSendMsgStatusEnum.CG.getCode());
                // 请求成功
                dtoBasicResult.setErrcode(BusinessCodeEnum.REQUEST_SUCCESS.getCode()).setErrmsg("发送消息成功");
            } else if( dtoBasicResult.getErrcode() == ACCESS_TOKEN_INVALID && StringUtils.isNotEmpty(dtoBasicResult.getErrmsg())
                    && StringUtils.contains(dtoBasicResult.getErrmsg(),ACCESS_TOKEN_MARK) ){
                // accessToken 失效,刷新 accessToken 值
                accessTokenUtil.getAccessToken();
            } else if(ACCESS_TOKEN_EXPIRED == dtoBasicResult.getErrcode()){
                // accessToken 过期,刷新 accessToken 值
                accessTokenUtil.getAccessToken();
            }
            // 记录发送时间并添加对象，并记录结果编码、结果消息
            mpMessage.setSendTmplTime(DateUtil.getCurrentDate());
            mpMessage.setErrcode(dtoBasicResult.getErrcode());
            mpMessage.setErrmsg(dtoBasicResult.getErrmsg());
            dao.save(mpMessage);
            // 返回对象
            return dtoBasicResult;
        } catch (Exception e) {
            // 记录日志和抛异常
            log.error(BusinessCodeEnum.SEND_SINGLE_MESSAGE_EXCEPTION.getMsg()+"-手机号："+phoneNumber, e);
            dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_SINGLE_MESSAGE_EXCEPTION.getCode()).setErrmsg(BusinessCodeEnum.SEND_SINGLE_MESSAGE_EXCEPTION.getMsg());
            mpMessage.setSendTmplTime(DateUtil.getCurrentDate());
            mpMessage.setErrcode(dtoBasicResult.getErrcode());
            mpMessage.setErrmsg(dtoBasicResult.getErrmsg());
            dao.save(mpMessage);
            return dtoBasicResult;
        }
    }

    /**
     * 群发消息 - 异步发送消息
     * @param request 群发消息体
     * @param user 用户信息
     * @return 返回基础结果
     */
    @Override
    @Async("asyncExecutor")
    public Future<DtoBasicResult> sendMessage(DtoSubscribeMessageRequest request, DtoUser user,String massJournalId){
        // 基础结果类
        DtoBasicResult dtoBasicResult = new DtoBasicResult();
        // 获取accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 设置用户
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
        MpMessage mpMessage = DtoSubscribeMessage.toPO(request);
        // 给消息赋值小程序ID，发送类型：群发 , 群发日志ID赋值 并添加发送消息
        mpMessage.setAppId(wechatConfig.getAppId());
        mpMessage.setSendType(SendMsgTypeEnum.MASS.getCode());
        mpMessage.setMassJournalId(massJournalId);
        // 发送订阅消息Url
        String sendSubscribeUrl = wechatConfig.getSendSubscribeUrl();
        // 记录日志
        log.info("接口名称：小程序群发订阅消息");
        log.info(String.format("接口参数：%s", sendSubscribeUrl));
        log.info(String.format("接口路径参数：access_token=%s",accessToken));
        log.info(String.format("接口JSON参数：%s",reqData));
        // 发送 http-post 请求
        try {
            // 设置 Url 带参
            String reqUrl = sendSubscribeUrl + "?access_token=" + accessToken;
            // 发送Post请求并接收字符串结果
            String result = HttpClientUtil.doPost(httpClient, reqUrl, reqData, headerMap);
            log.info("群发消息单条结果：手机号=" + phone + ",返回结果=" + result);
            // result 中包含<html>，那是腾讯接口服务器错误500
            if(StringUtils.contains(result,ERROR_MARK)){
                // 群发单条消息服务器异常
                BusinessCodeEnum messageSingleMessageServer = BusinessCodeEnum.SEND_MASS_MESSAGE_SINGLE_MESSAGE_SERVER;
                dtoBasicResult.setErrcode(messageSingleMessageServer.getCode()).setErrmsg(messageSingleMessageServer.getMsg());
                mpMessage.setSendTmplTime(DateUtil.getCurrentDate());
                mpMessage.setErrcode(dtoBasicResult.getErrcode());
                mpMessage.setErrmsg(dtoBasicResult.getErrmsg());
                dao.save(mpMessage);
                log.error("群发订阅消息："+JSON.toJSONString(dtoBasicResult));
                return new AsyncResult<>(dtoBasicResult);
            }
            // 解析字符串并转为 DtoBasicResult 对象
            dtoBasicResult = JSON.parseObject(result, DtoBasicResult.class);
            // errCode是 null | 0L 为成功，否则为失败
            if (dtoBasicResult.getErrcode() == null || BusinessCodeEnum.RECEIVE_SUCCESS.getCode().equals(dtoBasicResult.getErrcode())) {
                // 1、mpMessage 发送状态：成功标识
                mpMessage.setSendStatus(MpSendMsgStatusEnum.CG.getCode());
                // 2、记录日志
                dtoBasicResult.setErrcode(BusinessCodeEnum.REQUEST_SUCCESS.getCode()).setErrmsg(phone);
            } else {
                // accessToken 失效,刷新 accessToken 值
                if( dtoBasicResult.getErrcode() == ACCESS_TOKEN_INVALID && StringUtils.isNotEmpty(dtoBasicResult.getErrmsg())
                        && StringUtils.contains(dtoBasicResult.getErrmsg(),ACCESS_TOKEN_MARK) ){
                    accessTokenUtil.getAccessToken();
                } else if(ACCESS_TOKEN_EXPIRED == dtoBasicResult.getErrcode()){
                    // accessToken 过期,刷新 accessToken 值
                    accessTokenUtil.getAccessToken();
                }
            }
        } catch (Exception e) {
            // 记录群发单条消息异常
            BusinessCodeEnum singleMessageException = BusinessCodeEnum.SEND_MASS_MESSAGE_SINGLE_MESSAGE_EXCEPTION;
            dtoBasicResult.setErrcode(singleMessageException.getCode()).setErrmsg(singleMessageException.getMsg());
            log.error("群发单条消息异常："+JSON.toJSONString(dtoBasicResult));
        }
        // 记录发送时间并修改对象，并记录结果编码、结果消息
        mpMessage.setSendTmplTime(DateUtil.getCurrentDate());
        mpMessage.setErrcode(dtoBasicResult.getErrcode());
        mpMessage.setErrmsg(dtoBasicResult.getErrmsg());
        dao.save(mpMessage);
        // 返回基础结果
        return new AsyncResult<>(dtoBasicResult);
    }

    @Override
    public DtoBasicResult sendMassMessageByPhoneList(DtoSubscribeMessageRequest request) {
        // 打印入参日志
        log.info(String.format("群发消息入参", JSON.toJSONString(request)));
        // 声明基础返回结果类
        DtoBasicResult dtoBasicResult = new DtoBasicResult();
        // 检验秘钥是否合法
        CheckInParamUtil.checkSecret(dtoBasicResult, wechatConfig, request.getSecret());
        // 不为空，那就是非法的
        if (dtoBasicResult.getErrcode() != null) {
            return dtoBasicResult;
        }
        // 获取 accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request, dtoBasicResult, SendMsgTypeEnum.MASS);
        // 不为空，参数有为空
        if (dtoBasicResult.getErrcode() != null) {
            return dtoBasicResult;
        }
        synchronized(MpMessageServiceImpl.class){
            // 发送成功的手机号列表
            LinkedList<String> sendSuccessPhoneList = new LinkedList<>();
            // 需要发送的手机号列表
            List<String> phoneNumberList = request.getPhoneNumberList();
            request.setPhoneNumberList(null);
            // 根据手机号集合查询用户集合
            List<DtoUser> userList = userService.getDtoUserList(phoneNumberList);
            // 打印用户信息日志
            log.info("群发消息用户列表长度：" + userList.size());
            log.info("群发消息用户列表：" + JSON.toJSONString(userList));
            // 用户列表为空，发送订阅消息结果
            if (CollectionUtils.isEmpty(userList)) {
                return dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_MASS_MESSAGE_NOT_EXISTS_PHONE.getCode())
                        .setErrmsg(BusinessCodeEnum.SEND_MASS_MESSAGE_NOT_EXISTS_PHONE.getMsg());
            }
            // 添加群发日志
            MpMassJournal massJournal = new MpMassJournal();
            massJournal.setRequireContent(phoneNumberList.toString());
            massJournal.setRequireCount(phoneNumberList.size());
            massJournal.setValidCount(userList.size());
            massJournal.setCreateTime(DateUtil.getCurrentDate());
            try {
                massJournalService.save(massJournal);
            } catch (Exception e) {
                BusinessCodeEnum messageInsertJournalFail = BusinessCodeEnum.SEND_MASS_MESSAGE_INSERT_JOURNAL_FAIL;
                log.error(messageInsertJournalFail.getMsg(),e);
                return dtoBasicResult.setErrcode(messageInsertJournalFail.getCode()).setErrmsg(messageInsertJournalFail.getMsg());
            }

            // 异步 - 发送消息
            List<Future<DtoBasicResult>> futures = new LinkedList<>();
            userList.forEach(dtoUser -> {
                Future<DtoBasicResult> future = self_.sendMessage(request, dtoUser,massJournal.getId());
                futures.add(future);
            });
            // 发送消息结果汇总
            futures.forEach( future -> {
                try {
                    // 获取结果
                    DtoBasicResult basicResult = future.get();
                    // 消息发送成功，添加到 sendSuccessPhoneList，否则添加到 sendFailPhoneList
                    if(BusinessCodeEnum.REQUEST_SUCCESS.getCode().equals(basicResult.getErrcode())){
                        sendSuccessPhoneList.add(basicResult.getErrmsg());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    // 记录群发异步消息异常
                    BusinessCodeEnum messageException = BusinessCodeEnum.SEND_MASS_ASYNC_SINGLE_MESSAGE_EXCEPTION;
                    dtoBasicResult.setErrcode(messageException.getCode()).setErrmsg(messageException.getMsg());
                    log.error("群发异步消息获取结果异常："+JSON.toJSONString(messageException),e);
                }
            });
            // 需要发送的手机号中过滤发送成功的手机号，留下失败的手机号
            Collection collection = CollectionUtils.removeAll(phoneNumberList, sendSuccessPhoneList);
            // 给发送成功总数，发送失败总数，修改时间赋值。
            massJournal.setSuccessCount(sendSuccessPhoneList.size());
            massJournal.setFailureCount(collection.size());
            massJournal.setModifyTime(DateUtil.getCurrentDate());
            massJournalService.update(massJournal);
            // 设置返回消息结果
            dtoBasicResult.setErrcode(BusinessCodeEnum.REQUEST_SUCCESS.getCode())
                    .setErrmsg(String.format("群发消息结果：成功手机号列表-%s,失败手机号列表-%s",sendSuccessPhoneList,collection));
            log.info(String.format("群发消息结果：成功手机号列表-%s,失败手机号列表-%s",sendSuccessPhoneList,collection));
        }

        return dtoBasicResult;
    }
}
