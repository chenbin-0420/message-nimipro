package com.dhcc.miniprogram.api;

import com.dhcc.basic.util.Message;
import com.dhcc.miniprogram.dto.*;

/**
 * @author cb
 * @date 2020/6/28
 * description：小程序消息接口
 */
public interface MpMessageApi {

    /**
     * 验证消息来自微信服务器
     */
    void verifyMsgFromWechat();

    /**
     * 获取AccessToken
     * @param appid 小程序唯一凭证，即 AppID
     * @param appSecret 小程序唯一凭证密钥，即 AppSecret
     * @return 含 token 封装类
     */
    DtoAccessTokenResult getAccessToken(String appid, String appSecret);

    /**
     * 小程序登录
     * @param dtoLoginRequest 登录请求类
     * @return 用户唯一信息返回类
     */
    Message<DtoIdenInfoResult> userLogin(DtoLoginRequest dtoLoginRequest);

    /**
     * 指定人发送订阅消息
     * @param dtoSubscribeMessageRequest 小程序订阅消息请求类
     * @return 基础返回结果类
     */
    Message<DtoBasicResult> sendSubscribeMessageByPhone(DtoSubscribeMessageRequest dtoSubscribeMessageRequest);

    /**
     * 获取手机号
     * @param dtoPhoneNumberRequest 小程序获取手机号类
     * @return 手机号返回结果类
     */
    Message<DtoPhoneNumberResult> getPhoneNumber(DtoPhoneNumberRequest dtoPhoneNumberRequest);

    /**
     * 模板授权列表
     * @param getTemplateAuthRequest 模板授权请求
     * @return 返回模板授权结果
     */
    Message<DtoTemplateAuthResult> getTemplateAuthResult(DtoTemplateAuthRequest getTemplateAuthRequest);

    /**
     * 用户模板授权
     * @param templateAuthRequest 更新模板授权请求体
     * @return 返回模板授权结果
     */
    Message<DtoTemplateAuthResult> insertTemplateAuth(DtoUpdateTemplateAuthRequest templateAuthRequest);

    /**
     * 更新模板信息
     * @return 返回基础结果
     */
    Message<DtoBasicResult> updateTemplateList();

    /**
     * 群发订阅消息
     * @param request 群发消息请求类
     * @return DtoBasicResult
     */
    Message<DtoBasicResult> sendMassMessageByPhoneList(DtoSubscribeMessageRequest request);

    /**
     * 换绑手机号
     * @param dtoPhoneNumberRequest 获取手机号请求类
     * @return 手机号返回结果类
     */
    Message<DtoPhoneNumberResult> changePhone(DtoPhoneNumberRequest dtoPhoneNumberRequest);

    /**
     * 通过手机号列表获取模板授权用户列表
     * @param templateAuthAbbrRequests 模板授权用户请求
     * @return 模板授权用户列表
     */
    Message<DtoTemplateAuthPhoneResult> getTemplateAuthByPhoneList(DtoTemplateAuthPhoneRequest templateAuthAbbrRequests);

    /**
     * 通过手机号和模板ID获取模板授权用户列表
     * @param request 模板授权用户请求
     * @return 返回基础类
     */
    Message<DtoBasicResult> getTemplateAuthByCondition(DtoTemplateAuthPhoneCondRequest request);
}
