package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/2
 * description：返回手机号结果
 */
public class DtoPhoneNumberResult extends DtoBasicResult {
    /**
     * 手机号
     */
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DtoPhoneNumberResult() {}

    public DtoPhoneNumberResult(Integer errcode, String errmsg) {
        super(errcode, errmsg);
    }
}
