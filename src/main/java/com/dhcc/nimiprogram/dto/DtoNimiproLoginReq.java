package com.dhcc.nimiprogram.dto;

/**
 * @author cb
 * @date 2020/6/19
 * description：小程序登录请求类
 *              登录凭证校验
 */
public class DtoNimiproLoginReq {
    /**
     * appid 小程序appId
     */
    private String appid;
    /**
     * secret 小程序appSecret
     */
    private String secret;
    /**
     * js_code 登录时获取的 code
     */
    private String js_code;
    /**
     * grant_type 授权类型，此处只需填写 authorization_code
     */
    private String grant_type;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getJs_code() {
        return js_code;
    }

    public void setJs_code(String js_code) {
        this.js_code = js_code;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
}
