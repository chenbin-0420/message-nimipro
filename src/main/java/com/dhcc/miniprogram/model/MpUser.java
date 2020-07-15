package com.dhcc.miniprogram.model;

import com.dhcc.basic.annotations.FieldDefine;
import com.dhcc.basic.annotations.TableDefine;
import com.dhcc.basic.model.IdentifiedPo;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 测试用户-POJO
 * @author cb
 * @since 2020-06-24
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate(true)
@Proxy(lazy = false)
@Table(name = "mp_user", schema="dhcplat_zjzwfwzx", catalog="dhcplat_zjzwfwzx")
@TableDefine(title = "小程序用户")
public class MpUser extends IdentifiedPo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@FieldDefine(title = "小程序ID", number = 2)
	@Column(name = "app_id", length = 50, unique = false, nullable = true)
	private String appId;
	
	@FieldDefine(title = "用户唯一标识", number = 3)
	@Column(name = "open_id", length = 30, unique = false, nullable = true)
	private String openId;
	
	@FieldDefine(title = "会话密钥", number = 4)
	@Column(name = "session_key", length = 30, unique = false, nullable = true)
	private String sessionKey;
	
	@FieldDefine(title = "用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回", number = 5)
	@Column(name = "unionid", length = 50, unique = false, nullable = true)
	private String unionid;

	@FieldDefine(title = "手机号", number = 6)
	@Column(name = "phone_num", length = 11, unique = false, nullable = true)
	private String phoneNum;

	@FieldDefine(title = "手机区号", number = 7)
	@Column(name = "phone_prefix", length = 5, unique = false, nullable = true)
	private String phonePrefix;

	@FieldDefine(title = "创建人", number = 8)
	@Column(name = "create_user", length = 30, unique = false, nullable = true)
	private String createUser;

	@FieldDefine(title = "创建时间", number = 9)
	@Column(name = "create_time", length = 26, unique = false, nullable = true)
	private Date createTime;

	@FieldDefine(title = "修改人", number = 10)
	@Column(name = "modify_user", length = 30, unique = false, nullable = true)
	private String modifyUser;

	@FieldDefine(title = "修改时间", number = 11)
	@Column(name = "modify_time", length = 26, unique = false, nullable = true)
	private Date modifyTime;
	
	public MpUser() {
		
	}
	
	/**
	 * 读取属性：小程序ID
	 * @return
	 */
	public String getAppId() {
		return appId;
	}
	
	/**
	 * 设置属性：小程序ID
	 * @param appId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * 读取属性：用户唯一标识
	 * @return
	 */
	public String getOpenId() {
		return openId;
	}
	
	/**
	 * 设置属性：用户唯一标识
	 * @param openId
	 */
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	/**
	 * 读取属性：会话密钥
	 * @return
	 */
	public String getSessionKey() {
		return sessionKey;
	}
	
	/**
	 * 设置属性：会话密钥
	 * @param sessionKey
	 */
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	
	/**
	 * 读取属性：用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
	 * @return
	 */
	public String getUnionid() {
		return unionid;
	}
	
	/**
	 * 设置属性：用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
	 * @param unionid
	 */
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	/**
	 * 读取属性：手机号
	 * @return
	 */
	public String getPhoneNum() {
		return phoneNum;
	}

	/**
	 * 设置属性：手机号
	 * @param phoneNum
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	/**
	 * 读取属性：手机区号
	 * @return
	 */
	public String getPhonePrefix() {
		return phonePrefix;
	}

	/**
	 * 设置属性：手机区号
	 * @param phonePrefix
	 */
	public void setPhonePrefix(String phonePrefix) {
		this.phonePrefix = phonePrefix;
	}

	/**
	 * 创建人
	 * @return
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * 创建人
	 * @param createUser
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * 创建时间
	 * @return
	 */
	public Date getCreateTime() {
		return createTime != null ? (Date) createTime.clone() : null;
	}

	/**
	 * 创建时间
	 * @return
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = (Date)createTime.clone();
	}

	/**
	 * 修改人
	 * @return
	 */
	public String getModifyUser() {
		return modifyUser;
	}

	/**
	 * 修改人
	 * @param modifyUser
	 */
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	/**
	 * 修改时间
	 * @return
	 */
	public Date getModifyTime() {
		return modifyTime != null ? (Date) modifyTime.clone() : null;
	}

	/**
	 * 修改时间
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = (Date) modifyTime.clone();
	}
}
