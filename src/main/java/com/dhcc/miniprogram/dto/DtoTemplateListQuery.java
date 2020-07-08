package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/7
 * description：模板列表查询类
 */
public class DtoTemplateListQuery {
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 标题
     */
    private String title;
    /**
     * 类型
     */
    private Integer type;

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
