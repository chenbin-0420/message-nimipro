package com.dhcc.miniprogram.dao.impl;

import com.dhcc.basic.dao.hibernate.BaseDaoHibImpl;
import com.dhcc.miniprogram.dao.MpTemplateAuthDao;
import com.dhcc.miniprogram.dto.DtoTemplateAuthId;
import com.dhcc.miniprogram.dto.DtoTemplateId;
import com.dhcc.miniprogram.model.MpTemplateAuth;
import org.springframework.stereotype.Repository;

import java.util.List;
/*
dao层一般情况下与model是一对一的关系！【只负责】这一个model的增删改查，保持原子性以提高复用度。
所以要求此dao的【增删改的输入对象】和【查的输出对象】必需是此model。
不要把此model作为查询条件，但输出并非此model的方法也放在此dao内，这样会给调用者带来困扰！

那种多对象查询输出的，以主对象model来归入dao。
比如同时输出订单和客户信息的，以订单为主对象，客户信息只是manytoone自动带出的。

其他复杂统计报表查询的，建议单独一个reportXXX的dao。
*/

/**
 * 小程序订阅消息模板授权-DAO实现
 *
 * @author cb
 * @since 2020-07-02
 */
@Repository
public class MpTemplateAuthDaoImpl extends BaseDaoHibImpl<MpTemplateAuth, String> implements MpTemplateAuthDao {

    /**
     * 通过条件获取ID集合的SQL
     */
    private static final String ID_LIST_BY_PHONE_TEMPLATE_ID_SQL = "SELECT m.`id` FROM mp_template_auth m  WHERE m.`template_id` = ? AND m.`phone` = ?";
    private static final String GET_TEMPLATE_AUTH_BY_PHONE_SQL = "select type,title,template_id from mp_template_auth mp WHERE exists( select null from mp_user where open_id = mp.create_user ) and mp.`phone` = ? ";
    private static final String GET_TEMPLATE_AUTH_BY_PHONE_TEMPLATE_ID_SQL = "SELECT mp.`template_id` FROM mp_template_auth mp WHERE exists( select null from mp_user where open_id = mp.create_user ) and mp.`phone` = ? and mp.`template_id` = ? ";

    @Override
    public List<DtoTemplateAuthId> getIdListByPhoneTemplateId(String templateId, String phone) {
        return querySqlEntity(ID_LIST_BY_PHONE_TEMPLATE_ID_SQL, new String[]{templateId, phone},DtoTemplateAuthId.class,null);
    }

    @Override
    public List<DtoTemplateId> getTemplateAuthByPhone(String phone) {
        return querySqlEntity(GET_TEMPLATE_AUTH_BY_PHONE_SQL,new Object[]{ phone }, DtoTemplateId.class,null);
    }

    @Override
    public List<DtoTemplateId> getTemplateAuthByPhoneAndTemplateId(String phone, String templateId) {
        return querySqlEntity(GET_TEMPLATE_AUTH_BY_PHONE_TEMPLATE_ID_SQL,new Object[]{ phone, templateId }, DtoTemplateId.class,null);
    }
}
