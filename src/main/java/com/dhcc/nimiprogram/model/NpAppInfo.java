package com.dhcc.nimiprogram.model;

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
 * 小程序应用信息-POJO
 * @author cb
 * @since 2020-06-28
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate(true)
@Proxy(lazy = false)
@Table(name = "np_app_info", schema="dhcplat", catalog="dhcplat")
@TableDefine(title = "小程序应用信息")
public class NpAppInfo extends IdentifiedPo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@FieldDefine(title = "应用ID", number = 2)
	@Column(name = "app_id", length = 32, unique = false, nullable = true)
	private String appId;
	
	@FieldDefine(title = "应用秘钥", number = 3)
	@Column(name = "app_secret", length = 50, unique = false, nullable = true)
	private String appSecret;
	
	@FieldDefine(title = "创建时间", number = 4)
	@Column(name = "create_time", length = 26, unique = false, nullable = true)
	private Date createTime;
	
	@FieldDefine(title = "创建人", number = 5)
	@Column(name = "create_user", length = 32, unique = false, nullable = true)
	private String createUser;
	
	public NpAppInfo() {
		
	}
	
	/**
	 * 读取属性：应用ID
	 * @return
	 */
	public String getAppId() {
		return appId;
	}
	
	/**
	 * 设置属性：应用ID
	 * @param appId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * 读取属性：应用秘钥
	 * @return
	 */
	public String getAppSecret() {
		return appSecret;
	}
	
	/**
	 * 设置属性：应用秘钥
	 * @param appSecret
	 */
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
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
