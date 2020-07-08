package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/6
 * description：用户部分类
 */
public class DtoUser {
    /**
     * 小程序用户ID
     */
    private String openId;
    /**
     * 电话号码
     */
    private String phoneNum;

    public DtoUser() {
    }

    public DtoUser(String openId, String phoneNum) {
        this.openId = openId;
        this.phoneNum = phoneNum;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
