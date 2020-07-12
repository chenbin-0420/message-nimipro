package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/8
 * description：模板ID类
 */
public class DtoTemplateId {
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 标题
     */
    private String title;
    /**
     * 消息类型：2-一次性消息，3-长期性消息
     */
    private Integer type;

    public DtoTemplateId() {
    }

    public DtoTemplateId(String templateId, String title, Integer type) {
        this.templateId = templateId;
        this.title = title;
        this.type = type;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
