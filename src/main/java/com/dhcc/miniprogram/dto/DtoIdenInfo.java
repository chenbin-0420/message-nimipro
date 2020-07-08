package com.dhcc.miniprogram.dto;

import com.dhcc.miniprogram.model.MpUser;
import com.dhcc.miniprogram.util.DateUtil;

/**
 * @author cb
 * @date 2020/7/8
 * description：唯一信息类
 */
public class DtoIdenInfo extends DtoBasicResult{
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

    public static MpUser toPO(DtoIdenInfo idenInfo){
        MpUser user = new MpUser();
        user.setOpenId(idenInfo.getOpenid());
        user.setSessionKey(idenInfo.getSession_key());
        user.setUnionid(idenInfo.unionid);
        user.setCreateTime(DateUtil.getCurrentDate());
        return user;
    }
}
