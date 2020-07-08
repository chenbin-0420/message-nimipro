package com.dhcc.miniprogram.enums;

/**
 * @author cb
 * @date 2020/7/8
 *
 */
public enum BusinessCodeEnum {
    /**
     * 请求成功： REQUEST_SUCCESS
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
     * GET_PHONE_NUMBER_SESSION_KEY_EMPTY ：
     */
    REQUEST_SUCCESS(200,"请求成功"),
    VERIFY_SERVER_IO_EXCEPTION(600,"响应输出流异常"),
    VERIFY_SERVER_EXCEPTION(601,"验证服务器异常"),
    VERIFY_SERVER_FAIL_EXCEPTION(602,"验证消息来自微信服务器失败"),
    ACCESS_TOKEN_EXCEPTION(610,"获取AccessToken异常"),
    LOGIN_PARAM_EXCEPTION(620,"登录参数 code为空"),
    LOGIN_PHONE_NOT_EXISTS(621,""),
    LOGIN_PHONE_EXISTS(622,"已获取手机号"),
    LOGIN_SERVER_EXCEPTION(623,"登录异常"),
    GET_PHONE_NUMBER_PARAM_EMPTY(630,"检查手机号参数"),
    GET_PHONE_NUMBER_SESSION_KEY_EMPTY(631,"获取手机号参数sessionKey为空"),
    GET_PHONE_NUMBER_EXCEPTION(632,"获取手机号异常"),
    GET_PHONE_NUMBER_FAIL(633,"获取手机号失败"),
    TEMPLATE_AUTH_FAIL(640,"添加模板授权失败")
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
