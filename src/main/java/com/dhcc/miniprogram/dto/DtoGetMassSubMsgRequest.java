package com.dhcc.miniprogram.dto;

import java.util.List;

/**
 * @author cb
 * @date 2020/7/2
 * description：群发订阅消息请求体
 */
public class DtoGetMassSubMsgRequest {
    /**
     * phoneNumberList ：用户手机号集合
     */
    private List<String> phoneNumberList;
    /**
     * templateIdList ：是	所需下发的订阅模板id集合
     */
    private List<String> templateIdList;
    /**
     * page ：否	点击模板卡片后的跳转页面，仅限本小程序内的页面。
     * 支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     */
    private String page;
    /**
     * miniprogram_state ：跳转小程序类型
     * developer为开发版；
     * trial为体验版；
     * formal为正式版；
     * 默认为正式版
     */
    private String miniprogram_state;
    /**
     * 语言类型
     */
    private String lang;

    /**
     * data ：JSON 模板内容
     */
    private Object data;

}
