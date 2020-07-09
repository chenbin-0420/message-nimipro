package com.dhcc.miniprogram.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author cb
 * @date 2020/6/11
 * 微信配置类
 */
@Configuration
@ConfigurationProperties(prefix="wechat")
public class WechatConfig {

    /**
     * 令牌
     */
    private String wechatToken;
    /**
     * 小程序ID
     */
    private String appId;
    /**
     * 秘钥
     */
    private String secret;
    /**
     * 公众服务标题
     */
    private String externalTitle;
    /**
     * 公众服务描述
     */
    private String externalDesc;
    /**
     * 内部服务标题
     */
    private String internalTitle;
    /**
     * 内部服务描述
     */
    private String internalDesc;
    /**
     * 模式
     */
    private String mode;
    /**
     * 测试秘钥
     */
    private List<String> testSecretList;
    /**
     * 正式秘钥
     */
    private List<String> formalSecretList;

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

    public String getExternalTitle() {
        return externalTitle;
    }

    public void setExternalTitle(String externalTitle) {
        this.externalTitle = externalTitle;
    }

    public String getExternalDesc() {
        return externalDesc;
    }

    public void setExternalDesc(String externalDesc) {
        this.externalDesc = externalDesc;
    }

    public String getInternalTitle() {
        return internalTitle;
    }

    public void setInternalTitle(String internalTitle) {
        this.internalTitle = internalTitle;
    }

    public String getInternalDesc() {
        return internalDesc;
    }

    public void setInternalDesc(String internalDesc) {
        this.internalDesc = internalDesc;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> getTestSecretList() {
        return testSecretList;
    }

    public void setTestSecretList(List<String> testSecretList) {
        this.testSecretList = testSecretList;
    }

    public List<String> getFormalSecretList() {
        return formalSecretList;
    }

    public void setFormalSecretList(List<String> formalSecretList) {
        this.formalSecretList = formalSecretList;
    }
}
