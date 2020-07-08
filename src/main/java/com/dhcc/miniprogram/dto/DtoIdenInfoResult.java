package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/6/17
 * description：
 */
public class DtoIdenInfoResult extends DtoBasicResult{
    /**
     * 数据
     */
    private DtoIdenInfoFilter data;

    public DtoIdenInfoResult(){}

    public DtoIdenInfoResult(Integer errcode, String errmsg) {
        super(errcode, errmsg);
    }

    public DtoIdenInfoFilter getData() {
        return data;
    }

    public DtoIdenInfoResult setData(DtoIdenInfoFilter data) {
        this.data = data;
        return this;
    }
}
