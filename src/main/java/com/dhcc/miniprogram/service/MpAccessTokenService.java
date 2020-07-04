package com.dhcc.miniprogram.service;

import com.dhcc.miniprogram.dto.DtoAccessTokenResult;
import com.dhcc.miniprogram.model.MpAccessToken;
import com.dhcc.basic.service.BaseService;

/**
 * 小程序访问令牌-Service接口
 * @author cb
 * @since 2020-06-29
 */
public interface MpAccessTokenService extends BaseService<MpAccessToken, String> {

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * @param appid 小程序唯一凭证，即 AppID
     * @param appSecret 小程序唯一凭证密钥，即 AppSecret
     * @return DtoAccessTokenResult
     */
    DtoAccessTokenResult getAccessToken(String appid, String appSecret);
}
