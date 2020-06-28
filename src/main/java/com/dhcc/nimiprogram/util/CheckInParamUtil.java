package com.dhcc.nimiprogram.util;

import com.dhcc.basic.exception.BusinessException;
import com.dhcc.nimiprogram.dto.DtoNimiproSubMsgReq;
import com.dhcc.nimiprogram.dto.DtoNimiproLoginReq;
import org.apache.commons.lang.StringUtils;

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
    public static void checkInParam(String token, DtoNimiproSubMsgReq request) {
        String reason = "";
        if (StringUtils.isEmpty(token)) {
            reason += "access_token为空，";
        }
        if (StringUtils.isEmpty(request.getTouser())) {
            reason += "touser为空，";
        }
        if (StringUtils.isEmpty(request.getTemplate_id())) {
            reason += "template_id为空，";
        }
        if (request.getData() == null) {
            reason += "data为空，";
        }


        if (StringUtils.isNotEmpty(reason)) {
            reason = "订阅消息参数 " + reason;
            throw new BusinessException(reason.substring(0, reason.length() - 1));
        }
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
            reason = "后台接口调用凭据参数 " + reason;
            throw new BusinessException(reason.substring(0, reason.length() - 1));
        }
    }

    /**
     * 检查小程序登录凭证校验业务入参
     * @param login 登录凭证类
     */
    public static void checkInParam(DtoNimiproLoginReq login) {
        String reason = "";
        if (StringUtils.isEmpty(login.getAppid())) {
            reason += "appid为空，";
        }
        if (StringUtils.isEmpty(login.getSecret())) {
            reason += "secret为空，";
        }
        if (StringUtils.isEmpty(login.getJs_code())) {
            reason += "js_code为空，";
        }

        if (StringUtils.isNotEmpty(reason)) {
            reason = "登录凭证参数 " + reason;
            throw new BusinessException(reason.substring(0, reason.length() - 1));
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

}
