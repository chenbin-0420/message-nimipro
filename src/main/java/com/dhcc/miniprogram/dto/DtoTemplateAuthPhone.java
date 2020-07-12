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
    private List<DtoTemplateId> templateIdList;

    public DtoTemplateAuthPhone() {
    }

    public DtoTemplateAuthPhone(String phone, List<DtoTemplateId> templateIdList) {
        this.phone = phone;
        this.templateIdList = templateIdList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<DtoTemplateId> getTemplateIdList() {
        return templateIdList;
    }

    public void setTemplateIdList(List<DtoTemplateId> templateIdList) {
        this.templateIdList = templateIdList;
    }
}
