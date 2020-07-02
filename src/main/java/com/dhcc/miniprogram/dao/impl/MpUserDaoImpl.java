package com.dhcc.miniprogram.dao.impl;

import com.dhcc.basic.dao.hibernate.BaseDaoHibImpl;
import com.dhcc.miniprogram.dao.MpUserDao;
import com.dhcc.miniprogram.model.MpUser;
import org.springframework.stereotype.Repository;
/*
dao层一般情况下与model是一对一的关系！【只负责】这一个model的增删改查，保持原子性以提高复用度。
所以要求此dao的【增删改的输入对象】和【查的输出对象】必需是此model。
不要把此model作为查询条件，但输出并非此model的方法也放在此dao内，这样会给调用者带来困扰！

那种多对象查询输出的，以主对象model来归入dao。
比如同时输出订单和客户信息的，以订单为主对象，客户信息只是manytoone自动带出的。

其他复杂统计报表查询的，建议单独一个reportXXX的dao。
*/

/**
 * test_user-DAO实现
 * @author cb
 * @since 2020-06-23
 */
@Repository
public class MpUserDaoImpl extends BaseDaoHibImpl<MpUser, String> implements MpUserDao {
	
}
