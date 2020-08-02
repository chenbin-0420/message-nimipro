package com.dhcc.miniprogram.util;

import com.dhcc.basic.exception.BusinessException;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.BusinessCodeEnum;
import com.dhcc.miniprogram.enums.MpStatusEnum;
import com.dhcc.miniprogram.enums.SendMsgTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author cb
 * @date 2020/6/18
 * description：检查入参工具类
 */
public class CheckInParamUtil {

    /**
     * 群发最大总数为 1000
     */
    private static final int MASS_MAX_COUNT = 1000;
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
        if(SendMsgTypeEnum.SINGLE.equals(msgTypeEnum)){
            if(StringUtils.isEmpty(request.getPhoneNumber())){
                reason += "phoneNumber为空,";
            }
        } else {
            List<String> numberList = request.getPhoneNumberList();
            if(CollectionUtils.isEmpty(numberList)){
                reason += "phoneNumberList为空,";
            }
            if(numberList.size() > MASS_MAX_COUNT){
                reason += "phoneNumberList的长度不超过1000个手机号,";
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
            if(SendMsgTypeEnum.SINGLE.equals(msgTypeEnum)){
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
    public static void checkInParam(DtoAccessTokenResult dtoAccessTokenResult,String appId, String appSecret) {
        String reason = "";
        if (StringUtils.isEmpty(appId)) {
            reason += "appid为空，";
        }
        if (StringUtils.isEmpty(appSecret)) {
            reason += "secret为空，";
        }

        if (StringUtils.isNotEmpty(reason)) {
            // 获取 AccessToken 入参为空
            dtoAccessTokenResult.setErrcode(BusinessCodeEnum.ACCESS_TOKEN_IN_PARAM.getCode()).setErrmsg(BusinessCodeEnum.ACCESS_TOKEN_IN_PARAM.getMsg()+ reason);
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
     * 检查模板授权请求体入参
     * @param templateAuthResult 模板授权结果
     * @param dtoTemplateAuthRequest 模板授权请求体
     */
    public static void checkInParam(DtoTemplateAuthResult templateAuthResult,DtoUpdateTemplateAuthRequest dtoTemplateAuthRequest){
        String reason = "";
        String phone = dtoTemplateAuthRequest.getPhone();
        if(StringUtils.isEmpty(phone) || UNDEFINED.equals(phone)){
            reason += "phone为空,";
        }
        if(StringUtils.isEmpty(dtoTemplateAuthRequest.getOpenId())){
            reason += "openId为空,";
        }
        if(dtoTemplateAuthRequest.getTemplates() == null){
            reason += "templates为空,";
        }
        if(StringUtils.isNotEmpty(reason)){
            // 模板授权入参为空
            templateAuthResult.setErrcode(BusinessCodeEnum.TEMPLATE_AUTH_IN_PARAM_EMPTY.getCode())
                    .setErrmsg(BusinessCodeEnum.TEMPLATE_AUTH_IN_PARAM_EMPTY.getMsg()+reason);
        }
    }

    /**
     * FORMAL ：正式环境
     */
    public static final String FORMAL = "formal";

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
            return wrapTemplateAuthPhoneResult(templateAuthPhoneResult,BusinessCodeEnum.USER_AUTH_PHONE_PARAM_EMPTY);
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
            return wrapTemplateAuthPhoneResult(templateAuthPhoneResult,BusinessCodeEnum.AUTH_NOT_EXISTS_SECRET);
        } else {
            // 秘钥不为空，判断模式
            if(wechatConfig.getMode().equals(FORMAL)){
                // 正式模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getFormalSecretList().contains(secret)){
                    return wrapTemplateAuthPhoneResult(templateAuthPhoneResult,BusinessCodeEnum.AUTH_ERROR_SECRET);
                }
            } else {
                // 测试模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getTestSecretList().contains(secret)){
                    return wrapTemplateAuthPhoneResult(templateAuthPhoneResult,BusinessCodeEnum.AUTH_ERROR_SECRET);
                }
            }
        }
        return templateAuthPhoneResult;
    }

    /**
     * 封装模板授权手机号结果
     * @param templateAuthPhoneResult 模板授权手机号结果
     * @param businessCodeEnum 业务码枚举
     * @return 模板授权手机号结果
     */
    public static DtoTemplateAuthPhoneResult wrapTemplateAuthPhoneResult(DtoTemplateAuthPhoneResult templateAuthPhoneResult,BusinessCodeEnum businessCodeEnum){
        templateAuthPhoneResult.setErrcode(businessCodeEnum.getCode());
        templateAuthPhoneResult.setErrmsg(businessCodeEnum.getMsg());
        return templateAuthPhoneResult;
    }


    /**
     * 检查入参
     * @param request 请求类
     * @param dtoBasicResult 返回类
     * @param wechatConfig 微信配置类
     * @return 返回基础类
     */
    public static DtoBasicResult checkInParam(DtoTemplateAuthPhoneCondRequest request, DtoBasicResult dtoBasicResult, WechatConfig wechatConfig) {
        String reason = "";
        // 判断秘钥是否为空
        String secret = request.getSecret();
        if (StringUtils.isEmpty(secret)) {
            // 为空，抛没有权限
            return wrapBasicResult(dtoBasicResult,BusinessCodeEnum.AUTH_NOT_EXISTS_SECRET);
        } else {
            // 秘钥不为空，判断模式
            if(wechatConfig.getMode().equals(FORMAL)){
                // 正式模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getFormalSecretList().contains(secret)){
                    return wrapBasicResult(dtoBasicResult,BusinessCodeEnum.AUTH_ERROR_SECRET);
                }
            } else {
                // 测试模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getTestSecretList().contains(secret)){
                    return wrapBasicResult(dtoBasicResult,BusinessCodeEnum.AUTH_ERROR_SECRET);
                }
            }
        }
        if(StringUtils.isEmpty(request.getPhone())||UNDEFINED.equals(request.getPhone())){
            reason += "phone为空，";
        }
        if(StringUtils.isEmpty(request.getTemplateId())){
            reason += "templateId为空，";
        }
        if(StringUtils.isNotEmpty(reason)){
            dtoBasicResult.setErrcode(BusinessCodeEnum.USER_AUTH_TEMPLATE_IN_PARAM.getCode())
                    .setErrmsg(BusinessCodeEnum.USER_AUTH_TEMPLATE_IN_PARAM.getMsg()+reason.substring(0,reason.length()-1));
        }
        return dtoBasicResult;
    }

    /**
     * 封装基础返回类
     * @param dtoBasicResult 基础返回类及其子类
     * @param businessCodeEnum 业务码枚举
     * @return 基础返回类
     */
    public static DtoBasicResult wrapBasicResult(DtoBasicResult dtoBasicResult,BusinessCodeEnum businessCodeEnum){
        dtoBasicResult.setErrcode(businessCodeEnum.getCode());
        dtoBasicResult.setErrmsg(businessCodeEnum.getMsg());
        return dtoBasicResult;
    }

    /**
     * 检查秘钥
     * @param basicResult 基础结果
     * @param secret 秘钥
     * @return 基础结果类
     */
    public static void checkSecret(DtoBasicResult basicResult,WechatConfig wechatConfig,String secret){
        // 判断秘钥是否为空
        if (StringUtils.isEmpty(secret)) {
            // 为空，抛没有权限
            basicResult.setErrcode(BusinessCodeEnum.AUTH_NOT_EXISTS_SECRET.getCode()).setErrmsg(BusinessCodeEnum.AUTH_NOT_EXISTS_SECRET.getMsg());
        } else {
            // 秘钥不为空，判断模式
            if(FORMAL.equals(wechatConfig.getMode())){
                // 正式模式，判断是否相等，不相等秘钥不合法
                if(!wechatConfig.getFormalSecretList().contains(secret)){
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
