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
     * 是否订阅
     */
    private String isSub;

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

    public String getIsSub() {
        return isSub;
    }

    public void setIsSub(String isSub) {
        this.isSub = isSub;
    }
}
