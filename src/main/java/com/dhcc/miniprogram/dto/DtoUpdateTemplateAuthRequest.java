package com.dhcc.miniprogram.dto;

import com.dhcc.miniprogram.enums.TemplateStatusEnum;
import com.dhcc.miniprogram.model.MpTemplateAuth;
import com.dhcc.miniprogram.util.DateUtil;

import java.util.List;

/**
 * @author cb
 * @date 2020/7/29
 * description：更新模板授权请求
 */
public class DtoUpdateTemplateAuthRequest {
    /**
     * 小程序手机号
     */
    private String phone;
    /**
     * 小程序用户ID
     */
    private String openId;
    /**
     * 更新模板状态类
     */
    private List<DtoUpdateTemplateStatus> templates;

    public DtoUpdateTemplateAuthRequest() {
    }

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

    public List<DtoUpdateTemplateStatus> getTemplates() {
        return templates;
    }

    public void setTemplates(List<DtoUpdateTemplateStatus> templates) {
        this.templates = templates;
    }

    /**
     * DtoTemplateAuthRequest 转 MpTemplateAuth
     * @param templateAuthRequest 模板授权请求
     * @return 模板授权
     */
    public static MpTemplateAuth toPO(DtoUpdateTemplateAuthRequest templateAuthRequest){
        MpTemplateAuth templateAuth = new MpTemplateAuth();
        templateAuth.setPhone(templateAuthRequest.getPhone());
        templateAuth.setIsSub(TemplateStatusEnum.REJECT.getValue());
        templateAuth.setCreateUser(templateAuthRequest.getOpenId());
        templateAuth.setCreateTime(DateUtil.getCurrentDate());
        return templateAuth;
    }
}
