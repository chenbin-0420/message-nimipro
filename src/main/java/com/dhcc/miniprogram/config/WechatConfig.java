package com.dhcc.miniprogram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author cb
 * @date 2020/6/11
 * 微信配置类
 */
@Configuration
@ConfigurationProperties(prefix="wechat")
public class WechatConfig {

    private String wechatToken;
    private String appId;
    private String secret;
    private String testAppId;
    private String testSecret;

    public String getWechatToken() {
        return wechatToken;
    }

    public void setWechatToken(String wechatToken) {
        this.wechatToken = wechatToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTestAppId() {
        return testAppId;
    }

    public void setTestAppId(String testAppId) {
        this.testAppId = testAppId;
    }

    public String getTestSecret() {
        return testSecret;
    }

    public void setTestSecret(String testSecret) {
        this.testSecret = testSecret;
    }
}
