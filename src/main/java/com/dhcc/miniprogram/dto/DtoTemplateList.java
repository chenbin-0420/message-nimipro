package com.dhcc.miniprogram.dto;

import com.dhcc.miniprogram.model.MpTemplateList;
import com.dhcc.miniprogram.util.DateUtil;

/**
 * @author cb
 * @date 2020/7/3
 * description：获取模板列表
 */
public class DtoTemplateList {
    /**
     * 添加至帐号下的模板 id，发送小程序订阅消息时所需
     */
    private String priTmplId;
    /**
     * 模版标题
     */
    private String title;
    /**
     * 模版内容
     */
    private String content;
    /**
     * 模板内容示例
     */
    private String example;
    /**
     * 模版类型，2 为一次性订阅，3 为长期订阅
     */
    private Integer type;

    public String getPriTmplId() {
        return priTmplId;
    }

    public void setPriTmplId(String priTmplId) {
        this.priTmplId = priTmplId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * DtoTemplateList 转 MpTemplateList
     * @param getTemplateList 模板列表运输类
     * @return 模板列表
     */
    public static MpTemplateList toPO(DtoTemplateList getTemplateList){
        MpTemplateList templateList = new MpTemplateList();
        templateList.setTemplateId(getTemplateList.getPriTmplId());
        templateList.setTitle(getTemplateList.getTitle());
        templateList.setContent(getTemplateList.getContent());
        templateList.setExample(getTemplateList.getExample());
        templateList.setType(getTemplateList.getType());
        templateList.setCreateTime(DateUtil.getCurrentDate());
        return templateList;
    }
}
