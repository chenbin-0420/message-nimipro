package com.dhcc.miniprogram.enums;

/**
 * @author cb
 * @date 2020/6/29
 * description：是否删除
 */
public enum IsDelEnum {
    /**
     * T ：已删除
     * F ：未删除
     */
    T("T","已删除"),
    F("F","未删除");
    private String code;
    private String name;

    private IsDelEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
