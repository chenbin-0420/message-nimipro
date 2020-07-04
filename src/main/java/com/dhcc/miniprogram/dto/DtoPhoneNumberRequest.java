package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/6/30
 * description：获取手机号类
 */
public class DtoPhoneNumberRequest {
    /**
     * 加密算法的初始向量
     */
    private String iv;
    /**
     * 包括敏感数据在内的完整用户信息的加密数据
     */
    private String encryptedData;
    /**
     * 敏感数据对应的云 ID，开通云开发的小程序才会返回，可通过云调用直接获取开放数据
     */
    private String cloudId;
    /**
     * 微信小程序ID
     */
    private String openId;

    public DtoPhoneNumberRequest() {
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    @Override
    public String toString() {
        return "{ openId,"+openId+"iv="+iv+",encryptedData="+encryptedData+",cloudId="+cloudId+" }";
    }
}
