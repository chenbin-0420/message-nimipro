package com.dhcc.miniprogram.dto;

import com.alibaba.fastjson.JSON;
import com.dhcc.miniprogram.enums.MpSendMsgStatusEnum;
import com.dhcc.miniprogram.model.MpMessage;
import com.dhcc.miniprogram.util.DateUtil;

/**
 * @author cb
 * @date 2020/6/16
 * description：微信小程序订阅消息主体
 */
public class DtoSubscribeMessage {
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
    /**
     * 语言类型
     */
    private String lang;

    /**
     * data ：JSON 模板内容
     */
    private Object data;


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getMiniprogram_state() {
        return miniprogram_state;
    }

    public void setMiniprogram_state(String miniprogram_state) {
        this.miniprogram_state = miniprogram_state;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * DtoSubscribeMessage 转 MpMessage
     * @param subscribeMessage 小程序订阅消息
     * @return 小程序订阅消息
     */
    public static MpMessage toPO(DtoSubscribeMessage subscribeMessage){
        MpMessage mpMessage = new MpMessage();
        mpMessage.setCreateTime(DateUtil.getCurrentDate());
        mpMessage.setJumpPage(subscribeMessage.getPage());
        mpMessage.setLangType(subscribeMessage.getLang());
        mpMessage.setNimiproState(subscribeMessage.getMiniprogram_state());
        mpMessage.setSendTmplCont(JSON.toJSONString(subscribeMessage.getData()));
        mpMessage.setTmplId(subscribeMessage.getTemplate_id());
        mpMessage.setTouser(subscribeMessage.getTouser());
        mpMessage.setSendStatus(MpSendMsgStatusEnum.CLZ.getCode());
        return mpMessage;
    }
}
