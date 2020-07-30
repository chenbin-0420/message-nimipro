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
    SINGLE(1,"single"),
    MASS(2,"mass");

    private Integer code;
    private String name;

    SendMsgTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
