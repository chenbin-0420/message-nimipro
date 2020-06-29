package com.dhcc.nimiprogram.dto;

import com.alibaba.fastjson.JSON;
import com.dhcc.nimiprogram.model.NpMessage;
import com.dhcc.nimiprogram.util.DateUtil;
import com.dhcc.nimiprogram.util.NpSendMsgStatusEnum;

/**
 * @author cb
 * @date 2020/6/16
 * description：微信小程序订阅消息请求体
 */
public class DtoNpSubMsgReq {
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
     * DtoNpSubMsgReq 转 NpMessage
     * @param dtoNpSubMsgReq 小程序订阅消息
     * @return 小程序订阅消息
     */
    public static NpMessage toPO(DtoNpSubMsgReq dtoNpSubMsgReq){
        NpMessage npMessage = new NpMessage();
        npMessage.setCreateTime(DateUtil.getCurrentDate());
        npMessage.setJumpPage(dtoNpSubMsgReq.getPage());
        npMessage.setLangType(dtoNpSubMsgReq.getLang());
        npMessage.setNimiproState(dtoNpSubMsgReq.getMiniprogram_state());
        npMessage.setSendTmplCont(JSON.toJSONString(dtoNpSubMsgReq.getData()));
        npMessage.setTmplId(dtoNpSubMsgReq.getTemplate_id());
        npMessage.setTouser(dtoNpSubMsgReq.getTouser());
        npMessage.setSendStatus(NpSendMsgStatusEnum.CLZ.getCode());
        return npMessage;
    }
}
