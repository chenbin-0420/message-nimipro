package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/2
 * description：模板授权返回结果类
 */
public class DtoTemplateAuthResult extends DtoBasicResult {

    /**
     * 数据
     */
    private DtoTemplateAuth data;

    public DtoTemplateAuthResult(){}

    public DtoTemplateAuthResult(Integer errcode, String errmsg) {
        super(errcode, errmsg);
    }

    public DtoTemplateAuth getData() {
        return data;
    }

    public DtoTemplateAuthResult setData(DtoTemplateAuth data) {
        this.data = data;
        return this;
    }
}
