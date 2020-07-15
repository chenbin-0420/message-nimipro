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
     * GET_TEMPLATE_LIST ：获取当前帐号下的个人模板列表
     *  测试服务 请求外网地址 10.254.35.100
     *  端口号
     *    14445 正式
     *    14443 测试
     */
    TEST_URL("请求腾讯测试路径","http://10.254.35.100:14443"),
    FORMAL_URL("请求腾讯正式路径","http://10.254.35.100:14445"),
    LOCAL_URL("本地测试路径","https://api.weixin.qq.com"),
    GET_CODE2SESSION_URL("登录凭证校验",FORMAL_URL.getUrl()+"/sns/jscode2session"),
    GET_ACCESS_TOKEN_URL("获取小程序全局唯一后台接口调用凭据",FORMAL_URL.getUrl()+"/cgi-bin/token"),
    SEND_SUBSCRIBE_MESSAGE("小程序订阅消息请求类",FORMAL_URL.getUrl()+"/cgi-bin/message/subscribe/send"),
    GET_TEMPLATE_LIST("获取当前帐号下的个人模板列表",FORMAL_URL.getUrl()+"/wxaapi/newtmpl/gettemplate");

    MiniproUrlConfig(String name, String url) {
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

