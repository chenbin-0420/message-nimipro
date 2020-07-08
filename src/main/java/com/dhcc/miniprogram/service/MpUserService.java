package com.dhcc.miniprogram.service;

import com.dhcc.basic.service.BaseService;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.model.MpUser;

import java.util.List;

/**
 * test_user-Service接口
 * @author cb
 * @since 2020-06-23
 */
public interface MpUserService extends BaseService<MpUser, String> {

    /**
     * 小程序登录
     * @param loginRequest 登录请求类
     * @return 用户唯一信息返回类
     */
    DtoIdenInfoResult userLogin(DtoLoginRequest loginRequest);

    /**
     * 获取手机号
     * @param dtoPhoneNumberRequest 小程序获取手机号类
     * @return 手机号返回结果类
     */
    DtoPhoneNumberResult getPhoneNumber(DtoPhoneNumberRequest dtoPhoneNumberRequest);

    /**
     * 获取用户列表
     * @param phoneList 手机号列表
     * @param appId 应用id
     * @return 用户列表
     */
    List<DtoUser> getDtoUserList(List<String> phoneList, String appId);

    /**
     * 换绑手机号
     * @param dtoPhoneNumberRequest 获取手机号请求类
     * @return 手机号返回结果类
     */
    DtoPhoneNumberResult changePhone(DtoPhoneNumberRequest dtoPhoneNumberRequest);
}
