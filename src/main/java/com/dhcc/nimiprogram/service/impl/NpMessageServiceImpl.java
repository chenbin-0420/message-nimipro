package com.dhcc.nimiprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.dao.query.SimpleCondition;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.nimiprogram.config.NimiproUrlConfig;
import com.dhcc.nimiprogram.config.WechatConfig;
import com.dhcc.nimiprogram.dao.NpMessageDao;
import com.dhcc.nimiprogram.dto.*;
import com.dhcc.nimiprogram.model.NpAccessToken;
import com.dhcc.nimiprogram.model.NpMessage;
import com.dhcc.nimiprogram.model.NpUser;
import com.dhcc.nimiprogram.service.NpAccessTokenService;
import com.dhcc.nimiprogram.service.NpMessageService;
import com.dhcc.nimiprogram.service.NpUserService;
import com.dhcc.nimiprogram.util.*;
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
public class NpMessageServiceImpl extends BaseServiceImpl<NpMessageDao,NpMessage,String> implements NpMessageService {

    private static final Logger log = LoggerFactory.getLogger(NpMessageServiceImpl.class);

    @Autowired
    private NpUserService NpUserService;

    @Autowired
    private WechatConfig wechatConfig;

    @Lazy
    @Autowired
    private AccTkUtil accTkUtil;

    @Autowired
    private NpAccessTokenService npAccessTokenService;

    /**
     * 成功标识
     */
    private static final Long SUCCESS = 0L;

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
        if (signature.equals(SimpleAlgorUtil.genSignature(arr))) {
            // 返回微信需要字符串
            return echostr;
        } else {
            // 抛异常
            log.debug("验证消息的确来自微信服务器失败,参数数组{}",JSON.toJSON(arr));
            throw new BusinessException("验证消息的确来自微信服务器失败");
        }

    }

    @Override
    public DtoAccTkRst getAccessToken(String appid, String appSecret) {
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
            log.info("接口名称：" + NimiproUrlConfig.GET_ACCESS_TOKEN_URL.getName());
            log.info("接口参数：" + NimiproUrlConfig.GET_ACCESS_TOKEN_URL.getUrl());
            log.info("接口参数：" + paramMap);
            // 发送Get请求，并接收字符串的结果
            String result = HttpClientUtil.doGet(httpClient, NimiproUrlConfig.GET_ACCESS_TOKEN_URL.getUrl(), paramMap, headerMap);
            // 解析字符串为 DtoAccTkRst 对象
            DtoAccTkRst dtoAccTkRst = JSON.parseObject(result, DtoAccTkRst.class);
            // 访问令牌
            NpAccessToken npAccessToken = DtoAccTkRst.toPO(dtoAccTkRst);
            // 小程序ID
            npAccessToken.setAppid(wechatConfig.getAppId());
            // 秘钥
            npAccessToken.setSecret(wechatConfig.getSecret());
            // 创建时间
            npAccessToken.setCreateTime(DateUtil.getCurrentDate());
            // 是否删除 默认是F
            npAccessToken.setIsDel("F");
            // 添加 npAccessToken
            npAccessTokenService.save(npAccessToken);

            // 解析字符串并返回结果
            return dtoAccTkRst;

        } catch (Exception e) {
            // 记录日志和抛异常
            log.debug("获取微信Token异常\n", e);
            throw new BusinessException("获取微信Token异常");

        }
    }

    @Override
    public DtoNpIdenInfoRst login(DtoNpLoginReq login) {
        // 检查入参
        CheckInParamUtil.checkInParam(login);
        // 获取 httpClient
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        // 设置 Url 带参
        Map<String, String> paramMap = new HashMap<>(3);
        paramMap.put("appid", login.getAppid());
        paramMap.put("secret", login.getSecret());
        paramMap.put("js_code", login.getJs_code());
        paramMap.put("grant_type", GrantTypeEnum.AUTHORIZATION_CODE.getCode());
        // 设置请求体参数
        Map<String, String> headerMap = new HashMap<>(1);

        try {
            log.info("接口名称：" + NimiproUrlConfig.GET_CODE2SESSION_URL.getName());
            log.info("接口参数：" + NimiproUrlConfig.GET_CODE2SESSION_URL.getUrl());
            log.info("接口参数：" + paramMap);
            // 发送Get请求，并接收字符串的结果
            String jsonObj = HttpClientUtil.doGet(httpClient, NimiproUrlConfig.GET_CODE2SESSION_URL.getUrl(), paramMap, headerMap);
            DtoNpIdenInfoRst dtoNimiProIdInfoRst = JSON.parseObject(jsonObj, DtoNpIdenInfoRst.class);
            // 根据openId查询用户是否存在
            SimpleCondition sc = new SimpleCondition().addParm("openId",dtoNimiProIdInfoRst.getOpenid());
            NpUser user = NpUserService.findOne(sc);
            // 不存在添加用户
            if(user == null){
                NpUser NpUser = DtoNpIdenInfoRst.toPO(dtoNimiProIdInfoRst);
                NpUser.setAppId(login.getAppid());
                /* NpUser.setCreateUser(); */
                NpUserService.save(NpUser);
            }else{
                if(StringUtils.isNotEmpty(dtoNimiProIdInfoRst.getSession_key())){
                    // 修改sessionKey
                    user.setSessionKey(dtoNimiProIdInfoRst.getSession_key());
                }
                user.setModifyTime(DateUtil.getCurrentDate());
                NpUserService.update(user);
            }
            // 解析字符串并返回结果
            return dtoNimiProIdInfoRst;
        } catch (Exception e) {
            // 记录日志和抛异常
            log.debug("登录凭证校验异常",e);
            throw new BusinessException("登录凭证校验异常");
        }
    }

    @Override
    public DtoBasicRst sendNimiProSubMsg(DtoNpSubMsgReq request) {
        // 获取 accessToken
        String accessToken = accTkUtil.getAccessToken();
        // 检查入参
        CheckInParamUtil.checkInParam(accessToken, request);

        // 获取httpClient
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        // 将对象转为Json字符串
        String reqData = JSON.toJSONString(request);
        // 设置请求体参数
        HashMap<String, String> headerMap = new HashMap<>(1);

        // DtoNpSubMsgReq 转 NpMessage
        // 默认 sendStatus 是 CLZ-处理中
        NpMessage npMessage = DtoNpSubMsgReq.toPO(request);
        // 设置小程序ID
        npMessage.setAppId(wechatConfig.getAppId());
        // 保存
        dao.save(npMessage);

        // 发送 http-post 请求
        try {
            log.info("接口名称：" + NimiproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getName());
            log.info("接口参数：" + NimiproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl());
            log.info("接口路径参数：access_token=" + accessToken);
            log.info("接口JSON参数：" + reqData);
            // 设置 Url 带参
            String reqUrl = NimiproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl() + "?access_token=" + accessToken ;
            // 发送Post请求并接收字符串结果
            String result = HttpClientUtil.doPost(httpClient, reqUrl, reqData, headerMap);
            log.info("小程序发送订阅消息结果："+ result);
            // 解析字符串并转为 DtoBasicRst 对象
            DtoBasicRst dtoBasicRst = JSON.parseObject(result, DtoBasicRst.class);
            // errCode是 null | 0L 为成功，否则为失败
            if(dtoBasicRst.getErrcode() == null || dtoBasicRst.getErrcode().equals(SUCCESS)){
                // 记录成功
                npMessage.setSendStatus(NpSendMsgStatusEnum.CG.getCode());
            } else {
                // 记录失败
                npMessage.setSendStatus(NpSendMsgStatusEnum.SB.getCode());
            }
            // 记录发送时间
            npMessage.setSendTmplTime(DateUtil.getCurrentDate());
            // 修改对象
            dao.update(npMessage);
            // 返回对象
            return dtoBasicRst;
        } catch (Exception e) {
            // 记录日志和抛异常
            log.debug("小程序发送订阅消息异常", e);
            throw new BusinessException("小程序发送订阅消息异常");
        }
    }
}
