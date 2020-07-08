package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/2
 * description：返回手机号结果
 */
public class DtoPhoneNumberResult extends DtoBasicResult{

    private DtoPhoneNumber data;

    public DtoPhoneNumberResult(Integer errcode, String errmsg) {
        super(errcode, errmsg);
    }

    public DtoPhoneNumberResult() {
    }

    public DtoPhoneNumber getData() {
        return data;
    }

    public DtoPhoneNumberResult setData(DtoPhoneNumber data) {
        this.data = data;
        return this;
    }
}
