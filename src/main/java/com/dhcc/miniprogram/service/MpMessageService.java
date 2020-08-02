package com.dhcc.miniprogram.service;

import com.dhcc.miniprogram.dto.DtoBasicResult;
import com.dhcc.miniprogram.dto.DtoSubscribeMessageRequest;
import com.dhcc.miniprogram.dto.DtoUser;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;

/**
 * @author cb
 * @date 2020/6/28
 * description：小程序
 */
public interface MpMessageService {

    /**
     * 验证消息来自微信服务器
     * @param request 请求体
     * @return 输出验证字符串
     */
    String verifyMsgFromWechat(HttpServletRequest request);

    /**
     * 指定人发送订阅消息
     * @param request 订阅消息请求类
     * @return DtoBasicResult
     */
    DtoBasicResult sendSubscribeMessageByPhone(DtoSubscribeMessageRequest request);

    /**
     * 发送群发订阅消息
     * @param request 群发消息请求类
     * @return DtoBasicResult
     */
    DtoBasicResult sendMassMessageByPhoneList(DtoSubscribeMessageRequest request);

    /**
     * 异步 - 发送订阅消息
     * @param request 群发消息请求类
     * @param user 用户
     * @param massJournalId 群发日志ID
     * @return Future<DtoBasicResult>
     */
    Future<DtoBasicResult> sendMessage(DtoSubscribeMessageRequest request, DtoUser user,String massJournalId);
}
