package com.dhcc.nimiprogram.dto;

import java.util.HashMap;

/**
 * @author cb
 * @date 2020/6/16
 * description：微信小程序订阅消息请求体
 */
public class DtoNimiproSubMsgReq {
    /**
     * touser ：是	接收者（用户）的 openid
     */
    private String touser;
    /**
     * template_id ：是	所需下发的订阅模板id
     */
    private String template_id;
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
    private String lang;
    /**
     * data	：模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
     */
    private WeappSubscribeMessage data;

    public static DtoNimiproSubMsgReq newInstance(){
        return new DtoNimiproSubMsgReq();
    }

    public String getTouser() {
        return touser;
    }

    public DtoNimiproSubMsgReq setTouser(String touser) {
        this.touser = touser;
        return this;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public DtoNimiproSubMsgReq setTemplate_id(String template_id) {
        this.template_id = template_id;
        return this;
    }

    public String getPage() {
        return page;
    }

    public DtoNimiproSubMsgReq setPage(String page) {
        this.page = page;
        return this;
    }

    public String getMiniprogram_state() {
        return miniprogram_state;
    }

    public DtoNimiproSubMsgReq setMiniprogram_state(String miniprogram_state) {
        this.miniprogram_state = miniprogram_state;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public DtoNimiproSubMsgReq setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public WeappSubscribeMessage getData() {
        return data;
    }

    public DtoNimiproSubMsgReq buildData() {
        this.data = new WeappSubscribeMessage();
        return this;
    }

    public DtoNimiproSubMsgReq putData(String keyword, String key, String value) {
        data.put(keyword,new WeappSubscribeMessageItem(key,value));
        return this;
    }

    private static class WeappSubscribeMessage extends HashMap<String,WeappSubscribeMessageItem> {

        public WeappSubscribeMessage(){}

        public  WeappSubscribeMessage(String keyword, WeappSubscribeMessageItem item){
            this.put(keyword,item);
        }
    }

    private static class WeappSubscribeMessageItem extends HashMap<String,Object>{

        public WeappSubscribeMessageItem(){}

        public WeappSubscribeMessageItem(String key, String value) {
            this.put(key,value);
        }
    }

}
