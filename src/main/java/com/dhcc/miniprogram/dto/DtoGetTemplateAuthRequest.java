package com.dhcc.miniprogram.dto;

import java.util.List;

/**
 * @author cb
 * @date 2020/7/1
 * description：获取模板授权请求类
 */
public class DtoGetTemplateAuthRequest {
    /**
     * 小程序手机号
     */
    private String phone;
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 模板ID数组
     */
    private List<String> templateIdList;

    public DtoGetTemplateAuthRequest() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public List<String> getTemplateIdList() {
        return templateIdList;
    }

    public void setTemplateIdList(List<String> templateIdList) {
        this.templateIdList = templateIdList;
    }
}
