package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/1
 * description：模板授权结果类
 */
public class DtoGetTemplateAuthResult {
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String desc;
    /**
     * 类别
     */
    private Integer type;
    /**
     * 排序
     */
    private Integer order;
    /**
     * 是否订阅
     */
    private Integer isSub;

    public DtoGetTemplateAuthResult() {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getIsSub() {
        return isSub;
    }

    public void setIsSub(Integer isSub) {
        this.isSub = isSub;
    }
}
