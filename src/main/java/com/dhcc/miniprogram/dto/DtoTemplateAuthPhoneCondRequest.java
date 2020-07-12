package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/10
 * description：符合条件的用户授权模板请求
 */
public class DtoTemplateAuthPhoneCondRequest {
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * secret ：秘钥
     */
    private String secret;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
