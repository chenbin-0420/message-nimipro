package com.dhcc.nimiprogram.dto;

/**
 * @author cb
 * @date 2020/6/11
 * 基础响应类
 */
public class DtoBasicRst {
    /**
     * errcode ：错误码
     */
    private Long errcode;
    /**
     * errmsg ：错误信息
     */
    private String errmsg;

    public DtoBasicRst() {
    }

    public Long getErrcode() {
        return errcode;
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "{ errcode=" + errcode + ", errmsg=" + errmsg +"}";
    }
}
