package com.dhcc.miniprogram.dao;

import com.dhcc.basic.dao.BaseDao;
import com.dhcc.miniprogram.dto.DtoTemplateId;
import com.dhcc.miniprogram.model.MpTemplateAuth;

import java.util.List;

/**
 * 小程序订阅消息模板授权-DAO接口
 * @author cb
 * @since 2020-07-02
 */
public interface MpTemplateAuthDao extends BaseDao<MpTemplateAuth, String> {

    /**
     * 根据模板ID、手机号查询模板ID集合
     * @param templateId 模板ID
     * @param phone 手机号
     * @return 模板ID集合
     */
    List<Object[]> getIdListByCondition(String templateId, String phone);

    /**
     * 根据手机号查询模板ID列表
     * @param phone 手机号
     * @return 模板ID列表
     */
    List<DtoTemplateId> getTemplateAuthByPhone(String phone);

    /**
     * 根据手机号查询模板ID列表
     * @param phone 手机号
     * @param templateId 模板ID
     * @return 模板ID列表
     */
    List<DtoTemplateId> getTemplateAuthByPhoneAndTemplateId(String phone,String templateId);
}
