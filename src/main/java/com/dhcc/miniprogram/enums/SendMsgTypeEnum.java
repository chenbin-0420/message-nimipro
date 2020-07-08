package com.dhcc.miniprogram.enums;
/**
 * @author cb
 * @date 2020/7/5
 * 发送消息类型的枚举
 */
public enum SendMsgTypeEnum {
    /**
     * SINGLE 发送指定人消息
     * MASS 群发消息
     */
    SINGLE("single","发送指定人消息"),
    MASS("mass","群发消息");

    private String Code;
    private String name;

    private SendMsgTypeEnum(String code, String name) {
        Code = code;
        this.name = name;
    }

    public String getCode() {
        return Code;
    }

    public String getName() {
        return name;
    }
}
