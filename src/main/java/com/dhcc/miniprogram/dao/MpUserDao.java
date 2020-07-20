package com.dhcc.miniprogram.dao;

import com.dhcc.basic.dao.BaseDao;
import com.dhcc.miniprogram.dto.DtoUser;
import com.dhcc.miniprogram.dto.DtoUserId;
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
     * @return 用户列表
     */
    List<DtoUser> getDtoUserList(List<String> phoneList);

    /**
     * 通过手机号获取用户ID
     * @param phone 手机号
     * @return 手机号类
     */
    DtoUserId getUserIdByPhone(String phone);

    /**
     * 根据手机号获取openId
     * @param phone 手机号
     * @return openId
     */
    Object getOpenIdByPhone(String phone);
}
