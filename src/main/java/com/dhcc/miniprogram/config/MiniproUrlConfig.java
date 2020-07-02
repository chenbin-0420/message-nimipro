package com.dhcc.miniprogram.config;

/**
 * @author cb
 * @date 2020/6/23
 * description：
 */
public enum MiniproUrlConfig {
    /**
     *
     * GET_CODE2SESSION_URL : 登录凭证校验
     * GET_ACCESS_TOKEN_URL : 获取小程序全局唯一后台接口调用凭据
     * GET_SUBSCRIBE_MESSAGE : 小程序订阅消息请求类
     */
    GET_CODE2SESSION_URL("登录凭证校验","https://api.weixin.qq.com/sns/jscode2session"),
    GET_ACCESS_TOKEN_URL("获取小程序全局唯一后台接口调用凭据","https://api.weixin.qq.com/cgi-bin/token"),
    SEND_SUBSCRIBE_MESSAGE("小程序订阅消息请求类","https://api.weixin.qq.com/cgi-bin/message/subscribe/send");

    private MiniproUrlConfig(String name, String url) {
        this.name = name;
        this.url = url;
    }

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

