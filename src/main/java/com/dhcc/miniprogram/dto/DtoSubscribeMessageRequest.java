package com.dhcc.miniprogram.dto;

import java.util.List;

/**
 * @author cb
 * @date 2020/6/16
 * description：微信小程序订阅消息请求体
 */
public class DtoSubscribeMessageRequest extends  DtoSubscribeMessage{
    /**
     * phoneNumber ：用户手机号
     */
    private String phoneNumber;
    /**
     * phoneNumberList : 用户手机号集合
     */
    private List<String> phoneNumberList;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<String> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }
}
