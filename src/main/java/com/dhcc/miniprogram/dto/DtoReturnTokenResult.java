package com.dhcc.miniprogram.dto;

import com.dhcc.miniprogram.model.MpAccessToken;

/**
 * @author cb
 * @date 2020/6/19
 * description：AccessTokenResult 访问token结果类
 */
public class DtoReturnTokenResult {
    /**
     * access_token	获取到的凭证
     */
    private String access_token;
    /**
     * expires_in 凭证有效时间，单位：秒。目前是7200秒之内的值。
     */
    private Long expires_in;
    /**
     * errcode 错误码
     */
    private Integer errcode;
    /**
     * errmsg 错误信息
     */
    private String errmsg;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * DtoReturnTokenResult 转 MpAccessToken
     * @param dtoReturnTokenResult 获取访问令牌结果
     * @return 小程序访问令牌
     */
    public static MpAccessToken toPO(DtoReturnTokenResult dtoReturnTokenResult){
        MpAccessToken mpAccessToken = new MpAccessToken();
        mpAccessToken.setAccessToken(dtoReturnTokenResult.getAccess_token());
        mpAccessToken.setExpiresIn(dtoReturnTokenResult.getExpires_in());
        mpAccessToken.setErrcode(dtoReturnTokenResult.getErrcode());
        mpAccessToken.setErrmsg(dtoReturnTokenResult.getErrmsg());
        return mpAccessToken;
    }
}
