package com.dhcc.nimiprogram.dto;

import java.util.Date;

/**
 * @author cb
 * @date 2020/6/28
 * description：
 */
public class DtoNpAppInfo {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 应用秘钥
     */
    private String appSecret;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createUser;

    public DtoNpAppInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Date getCreateTime() {
        return (Date) createTime.clone();
    }

    public void setCreateTime(Date createTime) {
        this.createTime = (Date) createTime.clone();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

}
