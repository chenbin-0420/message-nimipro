package com.dhcc.miniprogram.dto;

import java.util.List;
/**
 * @author cb
 * @date 2020/7/1
 * description：模板授权结果类
 */
public class DtoTemplateAuth {
    /**
     * 模板ID集合
     */
    private List<String> templateIds;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String desc;

    public DtoTemplateAuth() {
    }

    public DtoTemplateAuth(String title, String desc,List<String> templateIds) {
        this.templateIds = templateIds;
        this.title = title;
        this.desc = desc;
    }

    public List<String> getTemplateIds() {
        return templateIds;
    }

    public void setTemplateIds(List<String> templateIds) {
        this.templateIds = templateIds;
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
}
