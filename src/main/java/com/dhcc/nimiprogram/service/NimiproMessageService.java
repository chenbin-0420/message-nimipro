package com.dhcc.nimiprogram.service;

import com.dhcc.nimiprogram.dto.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cb
 * @date 2020/6/28
 * description：小程序
 */
public interface NimiproMessageService {

    /**
     * 验证消息的确来自微信服务器
     * @param request 请求体
     * @return 返回腾讯验证字符串
     */
    String verifyMsgFromWechat(HttpServletRequest request);

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * @param appid 小程序唯一凭证，即 AppID
     * @param appSecret 小程序唯一凭证密钥，即 AppSecret
     * @return DtoAccTkRst
     */
    DtoAccTkRst getAccessToken(String appid, String appSecret);

    /**
     * 登录凭证校验
     * @param login 登录请求类
     * @return DtoNimiproIdInfoRst
     */
    DtoNimiproIdInfoRst login(DtoNimiproLoginReq login);

    /**
     * 发送订阅消息
     * @param token 访问令牌
     * @param request 小程序订阅消息请求类
     * @return DtoBasicResult
     */
    DtoBasicResult sendNimiProSubMsg(String token, DtoNimiproSubMsgReq request);
}