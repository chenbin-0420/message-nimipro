package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/8
 * description：手机号封装类
 */
public class DtoPhoneNumber {
    /**
     * 手机号
     */
    private String phone;

    public DtoPhoneNumber() {
    }

    public DtoPhoneNumber(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
