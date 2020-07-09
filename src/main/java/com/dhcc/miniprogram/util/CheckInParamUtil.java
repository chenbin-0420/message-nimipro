package com.dhcc.miniprogram.util;

import com.dhcc.basic.exception.BusinessException;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.BusinessCodeEnum;
import com.dhcc.miniprogram.enums.MpStatusEnum;
import com.dhcc.miniprogram.enums.SendMsgTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author cb
 * @date 2020/6/18
 * description：检查入参工具类
 */
public class CheckInParamUtil {

    /**
     * 检查小程序订阅消息业务的入参
     * @param token 访问token
     * @param request 小程序订阅消息请求体
     */
    public static DtoBasicResult checkInParam(String token, DtoSubscribeMessageRequest request, DtoBasicResult dtoBasicResult, SendMsgTypeEnum msgTypeEnum) {
        String reason = "";
        if (StringUtils.isEmpty(token)) {
            reason += "access_token为空，";
        }
        if (StringUtils.isEmpty(request.getTemplate_id())) {
            reason += "template_id为空，";
        }
        if (request.getData() == null) {
            reason += "data为空，";
        }
        if(SendMsgTypeEnum.SINGLE.getCode().equals(msgTypeEnum.getCode())){
            if(StringUtils.isEmpty(request.getPhoneNumber())){
                reason += "phoneNumber为空,";
            }
        } else {
            List<String> numberList = request.getPhoneNumberList();
            if(CollectionUtils.isEmpty(numberList)){
                reason += "phoneNumberList为空,";
            }
        }
        if(StringUtils.isEmpty(request.getLang())){
            // 默认 zh_CN 中文简体
            request.setLang("zh_CN");
        }
        if(StringUtils.isEmpty(request.getMiniprogram_state())){
            // 默认 formal 正式版
            request.setMiniprogram_state(MpStatusEnum.FORMAL.getCode());
        }
        if (StringUtils.isNotEmpty(reason)) {
            if(SendMsgTypeEnum.SINGLE.getCode().equals(msgTypeEnum.getCode())){
                reason = BusinessCodeEnum.SEND_SINGLE_MESSAGE_PARAM_EMPTY.getMsg() + reason;
                dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_SINGLE_MESSAGE_PARAM_EMPTY.getCode());
            } else {
                reason = BusinessCodeEnum.SEND_MASS_MESSAGE_PARAM_EMPTY.getMsg() + reason;
                dtoBasicResult.setErrcode(BusinessCodeEnum.SEND_MASS_MESSAGE_PARAM_EMPTY.getCode());
            }
            return dtoBasicResult.setErrmsg(reason.substring(0, reason.length() - 1));
        }
        return dtoBasicResult;
    }

    /**
     * 检查小程序全局唯一后台接口调用凭据（access_token）业务的入参
     * @param appId 小程序appId
     * @param appSecret 小程序唯一凭证密钥，即 appSecret
     */
    public static void checkInParam(String appId, String appSecret) {
        String reason = "";
        if (StringUtils.isEmpty(appId)) {
            reason += "appid为空，";
        }
        if (StringUtils.isEmpty(appSecret)) {
            reason += "secret为空，";
        }

        if (StringUtils.isNotEmpty(reason)) {
            reason = "获取AccessToken " + reason;
            throw new BusinessException(reason.substring(0, reason.length() - 1));
        }
    }

    /**
     * 检查小程序登录业务入参
     * @param login 小程序登录
     */
    public static void checkInParam(DtoIdenInfoResult idenInfoResult, DtoLoginRequest login) {
        if (StringUtils.isEmpty(login.getCode())) {
            idenInfoResult.setErrcode(BusinessCodeEnum.LOGIN_PARAM_EXCEPTION.getCode()).setErrmsg(BusinessCodeEnum.LOGIN_PARAM_EXCEPTION.getMsg());
        }
    }

    /**
     * 检查验证消息的确来自微信服务器业务参数
     * @param signature 微信签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     */
    public static void checkEmptyValue(String signature, String timestamp, String nonce) {
        String reason = "";
        if (StringUtils.isEmpty(signature)) {
            reason += "signature为空，";

        }
        if (StringUtils.isEmpty(timestamp)) {
            reason += "timestamp为空，";

        }
        if (StringUtils.isEmpty(nonce)) {
            reason += "nonce为空，";

        }

        if (StringUtils.isNotEmpty(reason)) {
            reason += "验证消息来源微信服务器参数 " + reason;
            throw new BusinessException(reason.substring(0, reason.length() - 1));

        }

    }

    /**
     * 前端传OpenId值为undefined
     */
    private static final String UNDEFINED = "undefined";
    /**
     * 检查获取手机号入参
     * @param numberResult 手机号结果
     * @param dtoPhoneNumberRequest 获取手机号入参
     */
    public static void checkInParam(DtoPhoneNumberResult numberResult,DtoPhoneNumberRequest dtoPhoneNumberRequest){
        String reason = "";
        // 获取openId
        String openId = dtoPhoneNumberRequest.getOpenId();
        if(StringUtils.isEmpty(openId) || UNDEFINED.equals(openId)){
            reason += "openId为空,";
        }
        if(StringUtils.isEmpty(dtoPhoneNumberRequest.getIv())){
            reason += "iv为空,";
        }
        if(StringUtils.isEmpty(dtoPhoneNumberRequest.getEncryptedData())){
            reason += "encryptedData为空,";
        }
        if(StringUtils.isNotEmpty(reason)){
            reason = "获取手机号参数 "+reason;
            numberResult.setErrcode(BusinessCodeEnum.GET_PHONE_NUMBER_PARAM_EMPTY.getCode()).setErrmsg(reason.substring(0,reason.length()-1));
        }
    }

    /**
     * 检查多个值空值
     * @param needMap 需排查的参数
     * @param bisContent 业务内容
     */
    public static void checkEmptyValue(Map<String,Object> needMap, String bisContent){
        StringBuilder reason = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = needMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            if(next.getValue() == null || StringUtils.isEmpty(next.getValue().toString())){
                reason.append(next.getKey()).append("为空,");
            }
        }

        if(StringUtils.isNotEmpty(reason.toString())){
            String reasonStr = bisContent + reason.toString();
            throw new BusinessException(reasonStr.substring(0,reasonStr.length()-1));
        }
    }

    /**
     * 构建一个map，并初始化map，为其设置一个key-val
     * @param key 键
     * @param val 值
     * @return map
     */
    public static Map<String,Object> buildMap(String key,Object val){
        HashMap<String, Object> map = new HashMap<>(1);
        map.put(key,val);
        return map;
    }

    /**
     * 检查模板授权请求体入参
     * @param dtoTemplateAuthRequest 模板授权请求体
     */
    public static void checkInParam(DtoTemplateAuthRequest dtoTemplateAuthRequest){
        String reason = "";
        if(StringUtils.isEmpty(dtoTemplateAuthRequest.getPhone())){
            reason += "phone为空,";
        }
        if(StringUtils.isEmpty(dtoTemplateAuthRequest.getOpenId())){
            reason += "openId为空,";
        }
        if(dtoTemplateAuthRequest.getTemplateIds() == null){
            reason += "templateIds为空,";
        }
        if(StringUtils.isNotEmpty(reason)){
            reason = "模板授权请求参数 "+reason;
            throw new BusinessException(reason.substring(0,reason.length()-1));
        }
    }

    /**
     * DEV ：开发模式
     */
    private static final String DEV = "dev";

    /**
     * 检查模板授权API请求入参
     * @param templateAuthPhoneRequest 模板授权手机号请求类
     * @param wechatConfig 微信配置
     * @param templateAuthPhoneResult 模板授权手机号结果类
     */
    public static DtoTemplateAuthPhoneResult checkInParam(DtoTemplateAuthPhoneRequest templateAuthPhoneRequest, WechatConfig wechatConfig, DtoTemplateAuthPhoneResult templateAuthPhoneResult){
        // 获取秘钥
        String secret = templateAuthPhoneRequest.getSecret();
        checkSecret(templateAuthPhoneResult,secret,wechatConfig);
        if(templateAuthPhoneResult.getErrcode() != null){
            return templateAuthPhoneResult;
        }
        if(CollectionUtils.isEmpty(templateAuthPhoneRequest.getPhoneNumberList())){
            templateAuthPhoneResult.setErrcode(BusinessCodeEnum.TEMPLATE_AUTH_PHONE_PARAM_EMPTY.getCode());
            templateAuthPhoneResult.setErrmsg(BusinessCodeEnum.TEMPLATE_AUTH_PHONE_PARAM_EMPTY.getMsg());
            return templateAuthPhoneResult;
        }
        return templateAuthPhoneResult;
    }

    /**
     * 检查秘钥
     * @param templateAuthPhoneResult 模板授权手机号结果
     * @param secret 秘钥
     * @param wechatConfig 微信配置类
     * @return 基础结果类
     */
    private static DtoTemplateAuthPhoneResult checkSecret(DtoTemplateAuthPhoneResult templateAuthPhoneResult,String secret,WechatConfig wechatConfig){
        // 判断秘钥是否为空
        if (StringUtils.isEmpty(secret)) {
            // 为空，抛没有权限
            templateAuthPhoneResult.setErrcode(BusinessCodeEnum.AUTH_NOT_EXISTS_SECRET.getCode());
            templateAuthPhoneResult.setErrmsg(BusinessCodeEnum.AUTH_NOT_EXISTS_SECRET.getMsg());
            return templateAuthPhoneResult;
        } else {
            // 秘钥不为空，判断模式
            if(wechatConfig.getMode().equals(DEV)){
                // 正式模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getFormalSecretList().contains(secret)){
                    templateAuthPhoneResult.setErrcode(BusinessCodeEnum.AUTH_ERROR_SECRET.getCode());
                    templateAuthPhoneResult.setErrmsg(BusinessCodeEnum.AUTH_ERROR_SECRET.getMsg());
                    return templateAuthPhoneResult;
                }
            } else {
                // 测试模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getTestSecretList().contains(secret)){
                    templateAuthPhoneResult.setErrcode(BusinessCodeEnum.AUTH_ERROR_SECRET.getCode());
                    templateAuthPhoneResult.setErrmsg(BusinessCodeEnum.AUTH_ERROR_SECRET.getMsg());
                    return templateAuthPhoneResult;
                }
            }
        }
        return templateAuthPhoneResult;
    }

}
