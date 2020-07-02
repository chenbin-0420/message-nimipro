package com.dhcc.miniprogram.api;

import com.dhcc.miniprogram.dto.*;

/**
 * @author cb
 * @date 2020/6/28
 * description：小程序消息接口
 */
public interface MpMessageApi {

    /**
     * 验证消息的确来自微信服务器
     */
    void verifyMsgFromWechat();

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * @param appid 小程序唯一凭证，即 AppID
     * @param appSecret 小程序唯一凭证密钥，即 AppSecret
     * @return DtoAccessTokenResult
     */
    DtoAccessTokenResult getAccessToken(String appid, String appSecret);

    /**
     * 登录凭证校验
     * @param login 登录请求类
     * @return DtoGetIdenInfoResult
     */
    DtoGetIdenInfoResult login(DtoGetLoginRequest login);

    /**
     * 发送订阅消息
     * @param request 小程序订阅消息请求类
     * @return DtoBasicResult
     */
    DtoBasicResult sendMiniproSubMsg(DtoGetSubMsgRequest request);

    /**
     * 小程序获取手机号
     * @param dtoGetPhoneNumRequest 小程序获取手机号类
     * @return DtoBasicResult
     */
    DtoBasicResult getPhoneNum(DtoGetPhoneNumRequest dtoGetPhoneNumRequest);

    /**
     * 小程序模板授权
     * @param dtoGetTemplateAuthResult 模板授权结果
     * @return DtoBasicResult
     */
    String getTemplateAuthResult(DtoGetTemplateAuthResult dtoGetTemplateAuthResult);
}
