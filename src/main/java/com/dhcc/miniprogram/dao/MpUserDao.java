package com.dhcc.miniprogram.dao;

import com.dhcc.basic.dao.BaseDao;
import com.dhcc.miniprogram.dto.DtoUser;
import com.dhcc.miniprogram.model.MpUser;

import java.util.List;

/**
 * test_user-DAO接口
 * @author cb
 * @since 2020-06-23
 */
public interface MpUserDao extends BaseDao<MpUser, String> {

    /**
     * 获取用户列表
     * @param phoneList 手机号列表
     * @param appId 应用id
     * @return 用户列表
     */
    List<DtoUser> getDtoUserList(List<String> phoneList,String appId);
	
}
