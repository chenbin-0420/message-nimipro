package com.dhcc.miniprogram.dto;

import java.util.List;

/**
 * @author cb
 * @date 2020/7/9
 * description：模板授权手机号结果类
 */
public class DtoTemplateAuthPhoneResult extends DtoBasicResult {
    /**
     * 模板授权手机号类
     */
    private List<DtoTemplateAuthPhone> data;

    public DtoTemplateAuthPhoneResult() {
    }

    public DtoTemplateAuthPhoneResult(Integer errcode, String errmsg) {
        super(errcode, errmsg);
    }

    public List<DtoTemplateAuthPhone> getData() {
        return data;
    }

    public void setData(List<DtoTemplateAuthPhone> data) {
        this.data = data;
    }
}
