package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.dao.query.SimpleCondition;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.miniprogram.config.MiniproUrlConfig;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dao.MpMessageDao;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.GrantTypeEnum;
import com.dhcc.miniprogram.enums.IsDelEnum;
import com.dhcc.miniprogram.enums.MpSendMsgStatusEnum;
import com.dhcc.miniprogram.model.MpAccessToken;
import com.dhcc.miniprogram.model.MpMessage;
import com.dhcc.miniprogram.model.MpUser;
import com.dhcc.miniprogram.service.MpAccessTokenService;
import com.dhcc.miniprogram.service.MpMessageService;
import com.dhcc.miniprogram.service.MpTemplateAuthService;
import com.dhcc.miniprogram.service.MpUserService;
import com.dhcc.miniprogram.util.AccessTokenUtil;
import com.dhcc.miniprogram.util.CheckInParamUtil;
import com.dhcc.miniprogram.util.DateUtil;
import com.dhcc.miniprogram.util.SimpleAlgorithmUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    @Lazy
    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @Autowired
    private MpAccessTokenService accessTokenService;

    @Lazy
    @Autowired
    private MpTemplateAuthService templateAuthService;

    /**
     * MP_RECEIVE_SUCCESS ：成功标识
     */
    private static final Long MP_RECEIVE_SUCCESS = 0L;

    /**
     * RESPONSE_SEND_SUCCESS ：响应成功标识
     * RESPONSE_SEND_FAIL ：发送失败
     * RESPONSE_NOT_EXISTS_USER ：用户不存在
     * RESPONSE_NOT_EXISTS_PHONE ：用户不存在手机号
     */
    private static final Long RESPONSE_SEND_SUCCESS = 200L;
    private static final Long RESPONSE_SEND_FAIL = 4000L;
    private static final Long RESPONSE_NOT_EXISTS_USER = 4001L;
    private static final Long RESPONSE_NOT_EXISTS_PHONE = 4001L;

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
    public DtoReturnTokenResult getAccessToken(String appid, String appSecret) {
        log.info("获取AccessToken：{ appid="+appid+",secret="+appSecret+"}");
        // 检查入参
        CheckInParamUtil.checkInParam(appid,appSecret);

        // 获取 httpClient
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        // 设置 Url 带参
        HashMap<String, String> paramMap = new HashMap<>(3);
        paramMap.put("grant_type", GrantTypeEnum.CLIENT_CREDENTIAL.getCode());
        paramMap.put("appid", appid);
        paramMap.put("secret", appSecret);
        // 设置请求体参数
        HashMap<String, String> headerMap = new HashMap<>(1);

        try {
            log.info("接口名称：" + MiniproUrlConfig.GET_ACCESS_TOKEN_URL.getName());
            log.info("接口参数：" + MiniproUrlConfig.GET_ACCESS_TOKEN_URL.getUrl());
            log.info("接口参数：" + paramMap);
            // 发送Get请求，并接收字符串的结果
            String result = HttpClientUtil.doGet(httpClient, MiniproUrlConfig.GET_ACCESS_TOKEN_URL.getUrl(), paramMap, headerMap);
            // 解析字符串为 DtoReturnTokenResult 对象
            DtoReturnTokenResult dtoReturnTokenResult = JSON.parseObject(result, DtoReturnTokenResult.class);
            // 访问令牌
            MpAccessToken mpAccessToken = DtoReturnTokenResult.toPO(dtoReturnTokenResult);
            // 小程序ID
            mpAccessToken.setAppid(wechatConfig.getAppId());
            // 秘钥
            mpAccessToken.setSecret(wechatConfig.getSecret());
            // 创建时间
            mpAccessToken.setCreateTime(DateUtil.getCurrentDate());
            // 是否删除 默认是F
            mpAccessToken.setIsDel(IsDelEnum.F.getCode());
            // 添加 mpAccessToken
            accessTokenService.save(mpAccessToken);

            // 解析字符串并返回结果
            return dtoReturnTokenResult;

        } catch (Exception e) {
            // 记录日志和抛异常
            log.debug("获取AccessToken异常", e);
            throw new BusinessException("获取微信Token异常");

        }
    }

    @Override
    public DtoReturnIdenInfoResult login(DtoGetLoginRequest login) {
        log.info("登录凭证校验入参："+JSON.toJSONString(login));
        // 检查入参
        CheckInParamUtil.checkInParam(login);
        // 获取 httpClient
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        // 小程序ID
        String appId = wechatConfig.getAppId();
        // 设置 Url 带参
        Map<String, String> paramMap = new HashMap<>(3);
        paramMap.put("appid", appId);
        paramMap.put("secret", wechatConfig.getSecret());
        paramMap.put("js_code", login.getCode());
        paramMap.put("grant_type", GrantTypeEnum.AUTHORIZATION_CODE.getCode());
        // 设置请求体参数
        Map<String, String> headerMap = new HashMap<>(1);

        try {
            log.info("接口名称：" + MiniproUrlConfig.GET_CODE2SESSION_URL.getName());
            log.info("接口参数：" + MiniproUrlConfig.GET_CODE2SESSION_URL.getUrl());
            log.info("接口参数：" + paramMap);
            // 发送Get请求，并接收字符串的结果
            String jsonObj = HttpClientUtil.doGet(httpClient, MiniproUrlConfig.GET_CODE2SESSION_URL.getUrl(), paramMap, headerMap);
            DtoReturnIdenInfoResult dtoNimiProIdInfoRst = JSON.parseObject(jsonObj, DtoReturnIdenInfoResult.class);
            // 根据openId查询用户是否存在
            SimpleCondition sc = new SimpleCondition().addParm("openId",dtoNimiProIdInfoRst.getOpenid());
            MpUser user = userService.findOne(sc);
            // 不存在添加用户
            if(user == null){
                MpUser mpUser = DtoReturnIdenInfoResult.toPO(dtoNimiProIdInfoRst);
                mpUser.setAppId(appId);
                /*mpUser.setCreateUser();*/
                userService.save(mpUser);
            }else{
                if(StringUtils.isNotEmpty(dtoNimiProIdInfoRst.getSession_key())){
                    // 修改sessionKey
                    user.setSessionKey(dtoNimiProIdInfoRst.getSession_key());
                }
                user.setModifyTime(DateUtil.getCurrentDate());
                userService.update(user);
            }
            // session_key 不要传到前端
            dtoNimiProIdInfoRst.setSession_key(null);
            // 解析字符串并返回结果
            return dtoNimiProIdInfoRst;
        } catch (Exception e) {
            // 记录日志和抛异常
            log.debug("登录凭证校验异常",e);
            throw new BusinessException("登录凭证校验异常");
        }
    }

    @Override
    public DtoReturnBasicResult sendMiniproSubMsg(DtoGetSubMsgRequest request) {
        log.info("小程序发送订阅消息入参："+JSON.toJSONString(request));
        DtoReturnBasicResult dtoReturnBasicResult;
        // 获取 accessToken
        String accessToken = accessTokenUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request);
        // 设置手机号查询条件
        String phoneNumber = request.getPhoneNumber();
        SimpleCondition sc = new SimpleCondition().addParm("phoneNum", phoneNumber);
        // 查询
        MpUser user = userService.findOne(sc);
        // 用户不存在返回信息
        if(user == null){
            // 返回用户不存在结果
            dtoReturnBasicResult = new DtoReturnBasicResult(RESPONSE_NOT_EXISTS_USER, phoneNumber + " 的用户不存在");
            // 记录日志
            log.info("小程序发送订阅消息失败结果：" + JSON.toJSONString(dtoReturnBasicResult));
            return dtoReturnBasicResult;
        } else {
            // 获取用户手机号
            String phoneNum = user.getPhoneNum();
            // 手机号为空
            if(StringUtils.isEmpty(phoneNum)){
                // 返回用户没有手机号结果
                dtoReturnBasicResult = new DtoReturnBasicResult(RESPONSE_NOT_EXISTS_PHONE, phoneNumber + " 的用户，手机号不存在");
                // 记录日志
                log.info("小程序发送订阅消息失败结果：" + JSON.toJSONString(dtoReturnBasicResult));
                return dtoReturnBasicResult;
            } else {
                // 手机号，不为空、用户有此手机号，订阅消息绑定OpenId
                request.setTouser(user.getOpenId());
            }
        }

        // 获取httpClient
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        // 将对象转为Json字符串
        String reqData = JSON.toJSONString(request);
        // 设置请求体参数
        HashMap<String, String> headerMap = new HashMap<>(1);

        // DtoGetSubMsgRequest 转 MpMessage
        // 默认 sendStatus 是 CLZ-处理中
        MpMessage mpMessage = DtoGetSubMsgRequest.toPO(request);
        // 设置小程序ID
        mpMessage.setAppId(wechatConfig.getAppId());
        // 保存
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
            // 解析字符串并转为 DtoReturnBasicResult 对象
            dtoReturnBasicResult = JSON.parseObject(result, DtoReturnBasicResult.class);
            // errCode是 null | 0L 为成功，否则为失败
            if(dtoReturnBasicResult.getErrcode() == null || dtoReturnBasicResult.getErrcode().equals(MP_RECEIVE_SUCCESS)){
                // 1、mpMessage 发送状态：成功标识
                mpMessage.setSendStatus(MpSendMsgStatusEnum.CG.getCode());
                // 2、dtoReturnBasicResult errcode：成功标识
                dtoReturnBasicResult.setErrcode(RESPONSE_SEND_SUCCESS);
                // 3、auth 修改是否订阅:未订阅、更新修改时间、修改人
//                MpTemplateAuth auth = templateAuthService.findByPhone(request.getPhoneNumber());
//                auth.setIsSub(IsSubEnum.FALSE.getCode());
//                auth.setModifyUser(request.getTouser());
//                auth.setModifyTime(DateUtil.getCurrentDate());
            } else {
                // 记录失败
                mpMessage.setSendStatus(MpSendMsgStatusEnum.SB.getCode());
                // 设置成功标识
                dtoReturnBasicResult.setErrcode(RESPONSE_SEND_FAIL);
            }
            // 记录发送时间
            mpMessage.setSendTmplTime(DateUtil.getCurrentDate());
            // 修改对象
            dao.update(mpMessage);
            // 返回对象
            return dtoReturnBasicResult;
        } catch (Exception e) {
            // 记录日志和抛异常
            log.debug("小程序发送订阅消息异常", e);
            throw new BusinessException("小程序发送订阅消息异常");
        }
    }
}
