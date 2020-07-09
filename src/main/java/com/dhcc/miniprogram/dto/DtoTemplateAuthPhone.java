package com.dhcc.miniprogram.dto;

import java.util.List;

/**
 * @author cb
 * @date 2020/7/8
 * description：模板授权手机号类
 */
public class DtoTemplateAuthPhone {
    /**
     * 手机号
     */
    private String phone;
    /**
     * 模板ID
     */
    private List<String> templateId;

    public DtoTemplateAuthPhone() {
    }

    public DtoTemplateAuthPhone(String phone, List<String> templateId) {
        this.phone = phone;
        this.templateId = templateId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getTemplateId() {
        return templateId;
    }

    public void setTemplateId(List<String> templateId) {
        this.templateId = templateId;
    }
}
