package com.dhcc.miniprogram.dto;

import com.dhcc.miniprogram.enums.IsSubEnum;
import com.dhcc.miniprogram.model.MpTemplateAuth;
import com.dhcc.miniprogram.util.DateUtil;

/**
 * @author cb
 * @date 2020/7/1
 * description：获取模板授权请求类
 */
public class DtoGetTemplateAuthRequest {
    /**
     * 小程序手机号
     */
    private String phone;
    /**
     * 小程序用户ID
     */
    private String openId;
    /**
     * 小程序模板ID
     */
    private String templateId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * DtoGetTemplateAuthRequest 转 MpTemplateAuth
     * @param templateAuthRequest 模板授权请求
     * @return 模板授权
     */
    public static MpTemplateAuth toPO(DtoGetTemplateAuthRequest templateAuthRequest){
        MpTemplateAuth templateAuth = new MpTemplateAuth();
        templateAuth.setPhone(templateAuthRequest.getPhone());
        templateAuth.setTemplateId(templateAuthRequest.templateId);
        templateAuth.setCreateUser(templateAuthRequest.getOpenId());
        templateAuth.setIsSub(IsSubEnum.TRUE.getCode());
        templateAuth.setCreateTime(DateUtil.getCurrentDate());
        return templateAuth;
    }
}
