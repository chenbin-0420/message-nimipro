package com.dhcc.miniprogram.service;

import com.dhcc.miniprogram.dto.DtoBasicResult;
import com.dhcc.miniprogram.dto.DtoSubscribeMessageRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cb
 * @date 2020/6/28
 * description：小程序
 */
public interface MpMessageService {

    /**
     * 验证消息的确来自微信服务器
     * @param request 请求体
     * @return 返回腾讯验证字符串
     */
    String verifyMsgFromWechat(HttpServletRequest request);

    /**
     * 发送订阅消息
     * @param request 小程序订阅消息请求类
     * @return DtoBasicResult
     */
    DtoBasicResult sendSubscribeMessageByPhone(DtoSubscribeMessageRequest request);
}
