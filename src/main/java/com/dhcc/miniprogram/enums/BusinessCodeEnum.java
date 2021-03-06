package com.dhcc.miniprogram.enums;

/**
 * @author cb
 * @date 2020/7/8
 *
 */
public enum BusinessCodeEnum {
    /**
     *
     * 成功接收：RECEIVE_SUCCESS
     * 请求成功：REQUEST_SUCCESS
     *
     * 验证服务器
     * VERIFY_SERVER_FAIL_EXCEPTION ：验证消息来自微信服务器失败
     * VERIFY_SERVER_IO_EXCEPTION ：响应输出流异常
     * VERIFY_SERVER_EXCEPTION ：验证服务器异常
     *
     * 获取AccessToken
     * ACCESS_TOKEN_EXCEPTION ：获取AccessToken异常
     *
     * 小程序登录
     * LOGIN_PHONE_NOT_EXISTS ：手机号不存在
     * LOGIN_PHONE_EXISTS ：手机号存在
     *
     * 获取手机号
     * GET_PHONE_NUMBER_PARAM_EMPTY ：检验获取手机号参数
     * GET_PHONE_NUMBER_SESSION_KEY_EMPTY ：获取手机号参数sessionKey为空
     * GET_PHONE_NUMBER_EXCEPTION ：获取手机号异常
     * GET_PHONE_NUMBER_FAIL ：获取手机号失败
     *
     * 模板授权
     * TEMPLATE_AUTH_FAIL ：添加模板授权失败
     *
     * 发送消息
     * SEND_MESSAGE_PRAT_FAIL ：消息发送部分失败
     * SEND_MESSAGE_ALL_FAIL ：消息发送全部失败
     *
     * 权限：
     * AUTH_NOT_EXISTS_SECRET ：没有权限
     * AUTH_ERROR_SECRET ：秘钥不合法
     * AUTH_CORRECT_SECRET_MSG ：秘钥正确
     *
     * 发送消息：
     * SEND_SINGLE_PARAM_EMPTY ：单条订阅消息参数为空
     * SEND_MASS_PARAM_EMPTY ：群发订阅消息参数为空
     * SEND_MESSAGE_NOT_EXISTS_PHONE ：接收消息手机号不存在
     * SEND_MESSAGE_SINGLE_MESSAGE_EXCEPTION ：发送消息异常
     *
     * 模板授权手机号：
     * USER_AUTH_PHONE_PARAM_EMPTY ：模板授权手机号请求参数 phoneNumberList为空
     */
    RECEIVE_SUCCESS(0,"接收成功状态"),
    REQUEST_SUCCESS(200,"请求成功"),
    VERIFY_SERVER_IO_EXCEPTION(600,"响应输出流异常"),
    VERIFY_SERVER_EXCEPTION(601,"验证服务器异常"),
    VERIFY_SERVER_FAIL_EXCEPTION(602,"验证消息来自微信服务器失败"),
    ACCESS_TOKEN_IN_PARAM(609,"获取AccessToken入参 "),
    ACCESS_TOKEN_EXCEPTION(610,"获取AccessToken异常"),
    // 登录
    LOGIN_PARAM_EXCEPTION(620,"登录参数 code为空"),
    LOGIN_PHONE_NOT_EXISTS(621,""),
    LOGIN_PHONE_EXISTS(622,"已获取手机号"),
    LOGIN_SERVER_EXCEPTION(623,"登录异常"),
    // 获取手机号
    GET_PHONE_NUMBER_PARAM_EMPTY(630,"检查手机号参数"),
    GET_PHONE_NUMBER_SESSION_KEY_EMPTY(631,"获取手机号参数sessionKey为空"),
    GET_PHONE_NUMBER_EXCEPTION(632,"获取手机号异常"),
    GET_PHONE_NUMBER_FAIL(633,"获取手机号失败"),
    CHANGE_PHONE_NUMBER_SAME(635,"换绑手机号一致"),
    CHANGE_PHONE_FAIL(636,"换绑手机号失败"),
    CHANGE_PHONE_EXCEPTION(637,"换绑手机号异常"),
    CHANGE_PHONE_SESSION_KEY_EMPTY(638,"换绑手机号参数 sessionKey为空"),
    // 模板授权
    TEMPLATE_AUTH_FAIL(640,"添加模板授权失败"),
    GET_TEMPLATE_LIST_EXCEPTION(641,"获取模板列表异常"),
    TEMPLATE_AUTH_IN_PARAM_EMPTY(642,"模板授权入参 "),
    // 秘钥
    AUTH_NOT_EXISTS_SECRET(651,"没有权限"),
    AUTH_ERROR_SECRET(652,"秘钥不合法"),
    // 指定人发送消息
    SEND_SINGLE_MESSAGE_PARAM_EMPTY(661,"单条订阅消息参数"),
    SEND_SINGLE_MESSAGE_NOT_EXISTS_PHONE(662,"接收单条订阅手机号不存在"),
    SEND_SINGLE_MESSAGE_EXCEPTION(663,"单条订阅消息发送异常"),
    SEND_SINGLE_MESSAGE_FAIL(664,"单条订阅消息发送失败"),
    SEND_SINGLE_MESSAGE_SERVER(665,"单条订阅消息发送微信小程序服务器异常"),
    // 群发消息
    SEND_MASS_MESSAGE_PARAM_EMPTY(671,"群发订阅消息参数"),
    SEND_MASS_MESSAGE_INSERT_JOURNAL_FAIL(673,"群发订阅消息添加群发日志失败"),
    SEND_MASS_MESSAGE_NOT_EXISTS_PHONE(674,"接收群发订阅消息手机号用户不存在"),
    SEND_MASS_MESSAGE_SINGLE_MESSAGE_EXCEPTION(675,"群发单条消息异常"),
    SEND_MASS_MESSAGE_SINGLE_MESSAGE_SERVER(676,"群发单条消息发送微信小程序服务器异常"),
    SEND_MASS_ASYNC_SINGLE_MESSAGE_EXCEPTION(678,"群发消息-异步发送异常"),
    // 用户授权模板
    USER_AUTH_PHONE_PARAM_EMPTY(681,"用户授权模板请求参数 phoneNumberList为空"),
    USER_AUTH_TEMPLATE_EMPTY(682,"用户无授权模板"),
    USER_AUTH_TEMPLATE_IN_PARAM(683,"用户模板授权入参 "),
    USER_AUTH_PHONE_EXCEPTION(689,"用户模板授权失败")
    ;

    BusinessCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
