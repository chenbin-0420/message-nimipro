package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/29
 * description：更新模板状态
 */
public class DtoUpdateTemplateStatus {
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 模板状态
     */
    private String status;

    public DtoUpdateTemplateStatus() {
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
