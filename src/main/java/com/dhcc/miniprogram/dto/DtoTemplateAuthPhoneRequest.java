package com.dhcc.miniprogram.dto;

import java.util.List;

/**
 * @author cb
 * @date 2020/7/8
 * description：模板授权手机号请求类
 */
public class DtoTemplateAuthPhoneRequest {
    /**
     * phoneNumberList ：用户手机号集合
     */
    private List<String> phoneNumberList;
    /**
     * secret ：秘钥
     */
    private String secret;

    public List<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
