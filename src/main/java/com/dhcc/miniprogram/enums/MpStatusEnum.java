package com.dhcc.miniprogram.enums;

/**
 * @author cb
 * @date 2020/6/24
 * description：
 */
public enum MpStatusEnum {
    /**
     * DEVELOPER : 开发版
     * TRIAL : 体验版
     * FORMAL : 正式版
     */
    DEVELOPER("开发版","developer"),
    TRIAL("体验版","trial"),
    FORMAL("正式版","formal");

    private String name;
    private String code;

    private MpStatusEnum(String name, String code){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
