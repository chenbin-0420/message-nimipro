package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/6/19
 * description：小程序登录请求类
 *              登录凭证校验
 */
public class DtoLoginRequest {
    /**
     * appid 小程序appId
     */
    private String appid;
    /**
     * secret 小程序appSecret
     */
    private String secret;
    /**
     * code 登录时获取的 code
     */
    private String code;
    /**
     * grantType 授权类型，此处只需填写 authorization_code
     */
    private String grantType;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
