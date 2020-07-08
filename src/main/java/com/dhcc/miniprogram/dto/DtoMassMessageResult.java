package com.dhcc.miniprogram.dto;

import java.util.List;

/**
 * @author cb
 * @date 2020/7/6
 * description：群发消息结果类
 */
public class DtoMassMessageResult {
    /**
     * 消息发送成功列表
     */
    private List<String> success;
    /**
     * 消息发送失败列表
     */
    private List<String> fail;
    /**
     * 手机号不存在列表
     */
    private List<String> notExistsPhone;

    public DtoMassMessageResult() {
    }

    public List<String> getSuccess() {
        return success;
    }

    public void setSuccess(List<String> success) {
        this.success = success;
    }

    public List<String> getFail() {
        return fail;
    }

    public void setFail(List<String> fail) {
        this.fail = fail;
    }

    public List<String> getNotExistsPhone() {
        return notExistsPhone;
    }

    public void setNotExistsPhone(List<String> notExistsPhone) {
        this.notExistsPhone = notExistsPhone;
    }
}
