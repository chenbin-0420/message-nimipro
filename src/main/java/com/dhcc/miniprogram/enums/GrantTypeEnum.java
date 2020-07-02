package com.dhcc.miniprogram.enums;

/**
 * @author cb
 * @date 2020/6/19
 * description：授权类型
 */
public enum GrantTypeEnum {
    /**
     * grant_type :
     *  authorization_code : 授权码
     *  client_credential : 客户端凭证
     */
    AUTHORIZATION_CODE("authorization_code"),
    CLIENT_CREDENTIAL("client_credential");

    private String code;

    private GrantTypeEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
