package com.dhcc.miniprogram.enums;

/**
 * @author cb
 * @date 2020/7/2
 * description：是否订阅枚举类
 */
public enum IsSubEnum {
    /**
     * 1 ：已订阅
     * 0 ：未订阅
     */
    TRUE(1,"已订阅"),
    FALSE(0,"未订阅");

    IsSubEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     *
     */


    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
