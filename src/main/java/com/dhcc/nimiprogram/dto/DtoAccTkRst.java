package com.dhcc.nimiprogram.dto;

/**
 * @author cb
 * @date 2020/6/19
 * description：AccessTokenResult 访问token结果类
 */
public class DtoAccTkRst {
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
    private Long errcode;
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

    public Long getErrcode() {
        return errcode;
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
