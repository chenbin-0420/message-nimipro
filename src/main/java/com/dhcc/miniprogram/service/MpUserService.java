package com.dhcc.miniprogram.service;

import com.dhcc.basic.service.BaseService;
import com.dhcc.miniprogram.dto.DtoIdenInfoResult;
import com.dhcc.miniprogram.dto.DtoLoginRequest;
import com.dhcc.miniprogram.dto.DtoPhoneNumberRequest;
import com.dhcc.miniprogram.dto.DtoPhoneNumberResult;
import com.dhcc.miniprogram.model.MpUser;

/**
 * test_user-Service接口
 * @author cb
 * @since 2020-06-23
 */
public interface MpUserService extends BaseService<MpUser, String> {

    /**
     * 登录凭证校验
     * @param login 登录请求类
     * @return DtoIdenInfoResult
     */
    DtoIdenInfoResult userLogin(DtoLoginRequest login);

    /**
     * 小程序获取手机号
     * @param dtoPhoneNumberRequest 小程序获取手机号类
     * @return DtoPhoneNumberResult
     */
    DtoPhoneNumberResult getPhoneNum(DtoPhoneNumberRequest dtoPhoneNumberRequest);
}
