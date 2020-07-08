package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/6/11
 * 基础响应类
 */
public class DtoBasicResult {
    /**
     * errcode ：错误码
     */
    private Integer errcode;
    /**
     * errmsg ：错误信息
     */
    private String errmsg;

    public DtoBasicResult() {
    }

    public DtoBasicResult(Integer errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public DtoBasicResult setErrcode(Integer errcode) {
        this.errcode = errcode;
        return this;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public DtoBasicResult setErrmsg(String errmsg) {
        this.errmsg = errmsg;
        return this;
    }

}
