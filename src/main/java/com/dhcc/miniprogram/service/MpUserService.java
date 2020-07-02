package com.dhcc.miniprogram.service;

import com.dhcc.basic.service.BaseService;
import com.dhcc.miniprogram.dto.DtoBasicResult;
import com.dhcc.miniprogram.dto.DtoGetPhoneNumRequest;
import com.dhcc.miniprogram.model.MpUser;

/**
 * test_user-Service接口
 * @author cb
 * @since 2020-06-23
 */
public interface MpUserService extends BaseService<MpUser, String> {

    /**
     * 小程序获取手机号
     * @param dtoGetPhoneNumRequest 小程序获取手机号类
     * @return DtoBasicResult
     */
    DtoBasicResult getPhoneNum(DtoGetPhoneNumRequest dtoGetPhoneNumRequest);
}
