package com.dhcc.miniprogram.dto;

import java.util.List;

/**
 * @author cb
 * @date 2020/7/2
 * description：模板授权返回结果类
 */
public class DtoTemplateAuthResult extends DtoBasicResult {
    /**
     * 数据
     */
    private List<DtoTemplateAuth> data;

    public DtoTemplateAuthResult(){}

    public DtoTemplateAuthResult(Integer errcode, String errmsg) {
        super(errcode, errmsg);
    }

    public List<DtoTemplateAuth> getData() {
        return data;
    }

    public DtoTemplateAuthResult setData(List<DtoTemplateAuth> data) {
        this.data = data;
        return this;
    }
}
