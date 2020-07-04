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
     * @return token返回类
     */
    DtoAccessTokenResult getAccessToken(String appid, String appSecret);

    /**
     * 登录凭证校验
     * @param dtoLoginRequest 登录请求类
     * @return 用户唯一信息返回类
     */
    DtoIdenInfoResult userLogin(DtoLoginRequest dtoLoginRequest);

    /**
     * 发送订阅消息
     * @param dtoSubscribeMessageRequest 小程序订阅消息请求类
     * @return 基础返回结果类
     */
    DtoBasicResult sendSubscribeMessageByPhone(DtoSubscribeMessageRequest dtoSubscribeMessageRequest);

    /**
     * 小程序获取手机号
     * @param dtoPhoneNumberRequest 小程序获取手机号类
     * @return 手机号返回结果类
     */
    DtoPhoneNumberResult getPhoneNum(DtoPhoneNumberRequest dtoPhoneNumberRequest);

    /**
     * 小程序模板授权
     * @param getTemplateAuthRequest 模板授权请求
     * @return 返回模板授权结果
     */
    DtoTemplateAuthResult getTemplateAuthResult(DtoTemplateAuthRequest getTemplateAuthRequest);

    /**
     * 添加模板授权给用户
     * @param getTemplateAuthRequest 模板授权请求体
     * @return 返回模板授权结果
     */
    DtoTemplateAuthResult insertTemplateAuth(DtoTemplateAuthRequest getTemplateAuthRequest);

    /**
     * 更新所有的信息模板
     * @return 返回基础结果
     */
    DtoBasicResult updateTemplateList();
}
