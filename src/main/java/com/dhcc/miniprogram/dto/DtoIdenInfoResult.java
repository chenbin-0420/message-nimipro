package com.dhcc.miniprogram.dto;

import com.dhcc.miniprogram.model.MpUser;
import com.dhcc.miniprogram.util.DateUtil;

/**
 * @author cb
 * @date 2020/6/17
 * description：
 */
public class DtoIdenInfoResult extends DtoBasicResult {
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
    public String toString() {
        return "{openid="+openid+", session_key="+session_key+", unionid="+unionid+", errcode=" + super.getErrcode() + ", errmsg=" + super.getErrmsg()+"}";
    }

    public static MpUser toPO(DtoIdenInfoResult nimiProIdInfoRst){
        MpUser user = new MpUser();
        user.setOpenId(nimiProIdInfoRst.getOpenid());
        user.setSessionKey(nimiProIdInfoRst.getSession_key());
        user.setUnionid(nimiProIdInfoRst.unionid);
        user.setCreateTime(DateUtil.getCurrentDate());
        return user;
    }

}
