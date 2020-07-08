package com.dhcc.miniprogram.enums;

/**
 * @author cb
 * @date 2020/6/29
 * 微信小程序发送消息状态枚举类
 */
public enum MpSendMsgStatusEnum {
    /**
     * CLZ ：处理中
     * CG ：成功
     * SB ：失败
     */
    CLZ("CLZ","处理中"),
    CG("CG","成功"),
    SB("SB","失败");

    /**
     * code ：编码
     */
    private String code;
    /**
     * name ：名称
     */
    private String name;

    MpSendMsgStatusEnum(String code, String name){
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
