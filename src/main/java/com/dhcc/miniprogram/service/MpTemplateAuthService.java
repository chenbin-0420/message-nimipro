package com.dhcc.miniprogram.service;

import com.dhcc.miniprogram.dto.DtoTemplateAuthRequest;
import com.dhcc.miniprogram.dto.DtoTemplateAuthResult;
import com.dhcc.miniprogram.model.MpTemplateAuth;
import com.dhcc.basic.service.BaseService;

/**
 * 小程序订阅消息模板授权-Service接口
 * @author cb
 * @since 2020-07-02
 */
public interface MpTemplateAuthService extends BaseService<MpTemplateAuth, String> {
    /**
     * 添加模板授权给用户
     * @param dtoTemplateAuthRequest 模板授权请求体
     * @return 模板授权返回结果类
     */
    DtoTemplateAuthResult insertTemplateAuth(DtoTemplateAuthRequest dtoTemplateAuthRequest);

    /**
     * 根据手机号查询模板授权
     * @param phone 手机号
     * @return 模板授权对象
     */
    MpTemplateAuth findByPhone(String phone);
}
