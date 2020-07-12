package com.dhcc.miniprogram.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;

import com.dhcc.basic.annotations.FieldDefine;
import com.dhcc.basic.annotations.TableDefine;
import com.dhcc.basic.model.IdentifiedPo;

/**
 * 小程序访问令牌-POJO
 * @author cb
 * @since 2020-06-29
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate(true)
@Proxy(lazy = false)
@Table(name = "mp_access_token", schema="dhcplat_zjzwfwzx", catalog="dhcplat_zjzwfwzx")
@TableDefine(title = "小程序访问令牌")
public class MpAccessToken extends IdentifiedPo implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldDefine(title = "开发者ID(AppID)", number = 2)
	@Column(name = "appid", length = 20, unique = false, nullable = true)
	private String appid;

	@FieldDefine(title = "开发者密码(AppSecret)", number = 3)
	@Column(name = "secret", length = 40, unique = false, nullable = true)
	private String secret;

	@FieldDefine(title = "创建时间", number = 4)
	@Column(name = "create_time", length = 26, unique = false, nullable = true)
	private Date createTime;

	@FieldDefine(title = "获取到的凭证", number = 5)
	@Column(name = "access_token", length = 200, unique = false, nullable = true)
	private String accessToken;

	@FieldDefine(title = "凭证有效时间，单位：秒", number = 6)
	@Column(name = "expires_in", length = 19, unique = false, nullable = true)
	private Long expiresIn;

	@FieldDefine(title = "错误返回码", number = 7)
	@Column(name = "errcode", length = 10, unique = false, nullable = true)
	private Integer errcode;

	@FieldDefine(title = "错误返回信息", number = 8)
	@Column(name = "errmsg", length = 100, unique = false, nullable = true)
	private String errmsg;

	@FieldDefine(title = "是否删除：T-已删除、F-未删除", number = 9)
	@Column(name = "is_del", length = 2, unique = false, nullable = true)
	private String isDel;

	@FieldDefine(title = "创建人", number = 10)
	@Column(name = "create_user", length = 32, unique = false, nullable = true)
	private String createUser;

	public MpAccessToken() {

	}

	/**
	 * 读取属性：开发者ID(AppID)
	 * @return
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * 设置属性：开发者ID(AppID)
	 * @param appid
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/**
	 * 读取属性：开发者密码(AppSecret)
	 * @return
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * 设置属性：开发者密码(AppSecret)
	 * @param secret
	 */
	public void setSecret(String secret) {
		this.secret = secret;
	}

	/**
	 * 读取属性：创建时间
	 * @return
	 */
	public Date getCreateTime() {
		return (Date) createTime.clone();
	}

	/**
	 * 设置属性：创建时间
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = (Date) createTime.clone();
	}

	/**
	 * 读取属性：获取到的凭证
	 * @return
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 设置属性：获取到的凭证
	 * @param accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * 读取属性：凭证有效时间，单位：秒
	 * @return
	 */
	public Long getExpiresIn() {
		return expiresIn;
	}

	/**
	 * 设置属性：凭证有效时间，单位：秒
	 * @param expiresIn
	 */
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	/**
	 * 读取属性：错误返回码
	 * @return
	 */
	public Integer getErrcode() {
		return errcode;
	}

	/**
	 * 设置属性：错误返回码
	 * @param errcode
	 */
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	/**
	 * 读取属性：错误返回信息
	 * @return
	 */
	public String getErrmsg() {
		return errmsg;
	}

	/**
	 * 设置属性：错误返回信息
	 * @param errmsg
	 */
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	/**
	 * 读取属性：是否删除：T-已删除、F-未删除
	 * @return
	 */
	public String getIsDel() {
		return isDel;
	}

	/**
	 * 设置属性：是否删除：T-已删除、F-未删除
	 * @param isDel
	 */
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	/**
	 * 读取属性：创建人
	 * @return
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * 设置属性：创建人
	 * @param createUser
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

}
