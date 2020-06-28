package com.dhcc.nimiprogram.service.impl;

import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.nimiprogram.dao.TestUserDao;
import com.dhcc.nimiprogram.model.TestUser;
import com.dhcc.nimiprogram.service.TestUserService;
import org.springframework.stereotype.Service;
/*
service层一般情况下与model/dao是一对一的关系！【主要负责】这一个model的增删改查，保持原子性以提高复用度。
所以要求此service的【增删改的输入对象】和【查的输出对象】必需是此model。
不要把此model作为查询条件，但输出并非此model的方法也放在此service内，这样会给调用者带来困扰！

注意BaseServiceImpl里已经有save/update/delete统一方法，直接使用即可。
这些方法里已经处理了：非空检查，唯一性检查，删除前被使用检查，树状表path处理。
不要自己再重复写！！！

可以重写beforeSave/beforeUpdate/beforeDelete方法，在里面补充增删改前的检查判断。
可以重写afterSave/afterUpdate/afterDelete方法，在里面调用其他service对其他model做增删改。

对其他model做增删改，应该使用它的service，而不是dao。避免业务逻辑被跳过。
对其他model做查询，可以直接使用它的dao。但最好统一使用service。

牢记事务是在service层控制的，
service关键方法上应该有@Transactional注解。
查询方法上则用@Transactional(readOnly=true)

*/

/**
 * test_user-Service实现
 * @author cb
 * @since 2020-06-23
 */
@Service
public class TestUserServiceImpl extends BaseServiceImpl<TestUserDao, TestUser, String> implements TestUserService {
	@Override
	protected void beforeSave(TestUser object) {
		//新增前
	}
	@Override
	protected void afterSave(TestUser object, TestUser savedObject) {
		//新增后
	}
	@Override
	protected void beforeUpdate(TestUser object) {
		//修改前
	}
	@Override
	protected void afterUpdate(TestUser object, TestUser savedObject) {
		//修改后
	}
	@Override
	protected void beforeDelete(TestUser object) {
		//删除前
	}
	@Override
	protected void afterDelete(TestUser object) {
		//删除后
	}
}
