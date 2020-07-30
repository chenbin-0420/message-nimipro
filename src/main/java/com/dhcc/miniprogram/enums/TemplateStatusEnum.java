package com.dhcc.miniprogram.enums;

/**
 * @author cb
 * @date 2020/7/29
 * 模板状态枚举
 */
public enum TemplateStatusEnum {
    /**
     * ACCEPT ：接收
     * REJECT ：拒接
     * BAN ：禁止
     * UNKNOWN ：未知
     */
    ACCEPT("accept",1),
    REJECT("reject",2),
    BAN("ban",3),
    UNKNOWN("unknown",4);

    private String name;
    private Integer value;

    TemplateStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    /**
     * 根据名称转换模板状态枚举
     * @param name 模板状态
     * @return 模板状态枚举
     */
    public static TemplateStatusEnum convertTemplateStatusEnumByName(String name){
        for (TemplateStatusEnum templateStatusEnum : TemplateStatusEnum.values()) {
            if(templateStatusEnum.name.equals(name)){
                return templateStatusEnum;
            }
        }
        return UNKNOWN;
    }
}
