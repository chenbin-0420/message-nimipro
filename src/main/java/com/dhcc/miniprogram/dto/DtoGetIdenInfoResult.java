package com.dhcc.miniprogram.dto;

import com.dhcc.miniprogram.model.MpUser;
import com.dhcc.miniprogram.util.DateUtil;

/**
 * @author cb
 * @date 2020/6/17
 * description：
 */
public class DtoGetIdenInfoResult extends DtoBasicResult {
    /**
     * openid ：用户唯一标识
     */
    private String openid;
    /**
     * session_key ：会话密钥
     */
    private String session_key;
    /**
     * unionid	用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    private String unionid;
    /**
     * errcode ：错误码
     */
    private Long errcode;
    /**
     * errmsg ：错误信息
     */
    private String errmsg;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public Long getErrcode() {
        return errcode;
    }

    @Override
    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    @Override
    public String getErrmsg() {
        return errmsg;
    }

    @Override
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "{openid="+openid+", session_key="+session_key+", unionid="+unionid+", errcode=" + errcode + ", errmsg=" + errmsg+"}";
    }

    public static MpUser toPO(DtoGetIdenInfoResult nimiProIdInfoRst){
        MpUser user = new MpUser();
        user.setOpenId(nimiProIdInfoRst.getOpenid());
        user.setSessionKey(nimiProIdInfoRst.getSession_key());
        user.setUnionid(nimiProIdInfoRst.unionid);
        user.setCreateTime(DateUtil.getCurrentDate());
        return user;
    }

}
