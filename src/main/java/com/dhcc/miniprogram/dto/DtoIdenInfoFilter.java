package com.dhcc.miniprogram.dto;

/**
 * @author cb
 * @date 2020/7/8
 * description：用户唯一信息过滤类
 */
public class DtoIdenInfoFilter {
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

    public DtoIdenInfoFilter setSession_key(String session_key) {
        this.session_key = session_key;
        return this;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    /**
     * 过滤属性
     * @param idenInfo 唯一信息
     * @return 唯一信息过滤类
     */
    public static DtoIdenInfoFilter toFilter(DtoIdenInfo idenInfo){
        DtoIdenInfoFilter filter = new DtoIdenInfoFilter();
        filter.setOpenid(idenInfo.getOpenid());
        filter.setSession_key(idenInfo.getSession_key());
        filter.setUnionid(idenInfo.getUnionid());
        return filter;
    }
}
