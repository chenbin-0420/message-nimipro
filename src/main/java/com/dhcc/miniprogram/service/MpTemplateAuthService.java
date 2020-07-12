package com.dhcc.miniprogram.service;

import com.dhcc.basic.service.BaseService;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.model.MpTemplateAuth;

/**
 * 小程序订阅消息模板授权-Service接口
 * @author cb
 * @since 2020-07-02
 */
public interface MpTemplateAuthService extends BaseService<MpTemplateAuth, String> {
    /**
     * 用户模板授权
     * @param dtoTemplateAuthRequest 模板授权请求
     * @return 返回模板授权结果
     */
    DtoTemplateAuthResult insertTemplateAuth(DtoTemplateAuthRequest dtoTemplateAuthRequest);

    /**
     * 获取有效模板
     * @return 模板授权结果
     */
    DtoTemplateAuthResult getTemplateAuthResult();

    /**
     * 根据手机号查询模板授权
     * @param phone 手机号
     * @return 模板授权对象
     */
    MpTemplateAuth findByPhone(String phone);

    /**
     * 通过手机号列表获取模板授权用户列表
     * @param templateAuthAbbrRequests 模板授权用户请求
     * @return 模板授权用户列表
     */
    DtoTemplateAuthPhoneResult getTemplateAuthByPhoneList(DtoTemplateAuthPhoneRequest templateAuthAbbrRequests);

    /**
     * 通过手机号和模板ID获取模板授权用户列表
     * @param request 模板授权用户请求
     * @return 返回基础类
     */
    DtoBasicResult getTemplateAuthByCondition(DtoTemplateAuthPhoneCondRequest request);

}
