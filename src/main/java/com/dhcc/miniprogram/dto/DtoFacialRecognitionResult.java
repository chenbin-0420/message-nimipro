package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2021/3/31
 * description：
 */
public class DtoFacialRecognitionResult {
    /**
     *  错误码
     * 0 表示成功
     * 84001 非法identity_id
     * 84002 用户信息过期
     * 84003 用户信息不存在
     */
    private String errcode;
    /**
     *  错误信息
     */
    private String errmsg;
    /**
     * 	认证结果
     */
    private String identify_ret;
    /**
     * 认证时间
     */
    private String identify_time;
    /**
     * 用户读的数字（如是读数字）
     */
    private String validate_data;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getIdentify_ret() {
        return identify_ret;
    }

    public void setIdentify_ret(String identify_ret) {
        this.identify_ret = identify_ret;
    }

    public String getIdentify_time() {
        return identify_time;
    }

    public void setIdentify_time(String identify_time) {
        this.identify_time = identify_time;
    }

    public String getValidate_data() {
        return validate_data;
    }

    public void setValidate_data(String validate_data) {
        this.validate_data = validate_data;
    }
}
