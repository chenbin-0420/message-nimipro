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
 * 小程序订阅消息-POJO
 * @author cb
 * @since 2020-06-28
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate(true)
@Proxy(lazy = false)
@Table(name = "mp_message", schema="dhcplat_zjzwfwzx", catalog="dhcplat_zjzwfwzx")
@TableDefine(title = "小程序订阅消息")
public class MpMessage extends IdentifiedPo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@FieldDefine(title = "app_id", number = 2)
	@Column(name = "app_id", length = 32, unique = false, nullable = true)
	private String appId;
	
	@FieldDefine(title = "channel", number = 3)
	@Column(name = "channel", length = 2, unique = false, nullable = true)
	private String channel;
	
	@FieldDefine(title = "create_time", number = 4)
	@Column(name = "create_time", length = 26, unique = false, nullable = true)
	private Date createTime;
	
	@FieldDefine(title = "jump_page", number = 5)
	@Column(name = "jump_page", length = 255, unique = false, nullable = true)
	private String jumpPage;
	
	@FieldDefine(title = "lang_type", number = 6)
	@Column(name = "lang_type", length = 5, unique = false, nullable = true)
	private String langType;
	
	@FieldDefine(title = "nimipro_state", number = 7)
	@Column(name = "nimipro_state", length = 10, unique = false, nullable = true)
	private String nimiproState;
	
	@FieldDefine(title = "send_status", number = 8)
	@Column(name = "send_status", length = 3, unique = false, nullable = true)
	private String sendStatus;
	
	@FieldDefine(title = "send_tmpl_cont", number = 9)
	@Column(name = "send_tmpl_cont", length = 375, unique = false, nullable = true)
	private String sendTmplCont;
	
	@FieldDefine(title = "send_tmpl_time", number = 10)
	@Column(name = "send_tmpl_time", length = 26, unique = false, nullable = true)
	private Date sendTmplTime;
	
	@FieldDefine(title = "sender", number = 11)
	@Column(name = "sender", length = 32, unique = false, nullable = true)
	private String sender;
	
	@FieldDefine(title = "tmpl_id", number = 12)
	@Column(name = "tmpl_id", length = 50, unique = false, nullable = true)
	private String tmplId;
	
	@FieldDefine(title = "touser", number = 13)
	@Column(name = "touser", length = 30, unique = false, nullable = true)
	private String touser;
	
	public MpMessage() {
		
	}
	
	/**
	 * 读取属性：app_id
	 * @return
	 */
	public String getAppId() {
		return appId;
	}
	
	/**
	 * 设置属性：app_id
	 * @param appId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 * 读取属性：channel
	 * @return
	 */
	public String getChannel() {
		return channel;
	}
	
	/**
	 * 设置属性：channel
	 * @param channel
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	/**
	 * 读取属性：create_time
	 * @return
	 */
	public Date getCreateTime() {
		return (Date) createTime.clone();
	}
	
	/**
	 * 设置属性：create_time
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = (Date) createTime.clone();
	}
	
	/**
	 * 读取属性：jump_page
	 * @return
	 */
	public String getJumpPage() {
		return jumpPage;
	}
	
	/**
	 * 设置属性：jump_page
	 * @param jumpPage
	 */
	public void setJumpPage(String jumpPage) {
		this.jumpPage = jumpPage;
	}
	
	/**
	 * 读取属性：lang_type
	 * @return
	 */
	public String getLangType() {
		return langType;
	}
	
	/**
	 * 设置属性：lang_type
	 * @param langType
	 */
	public void setLangType(String langType) {
		this.langType = langType;
	}
	
	/**
	 * 读取属性：nimipro_state
	 * @return
	 */
	public String getNimiproState() {
		return nimiproState;
	}
	
	/**
	 * 设置属性：nimipro_state
	 * @param nimiproState
	 */
	public void setNimiproState(String nimiproState) {
		this.nimiproState = nimiproState;
	}
	
	/**
	 * 读取属性：send_status
	 * @return
	 */
	public String getSendStatus() {
		return sendStatus;
	}
	
	/**
	 * 设置属性：send_status
	 * @param sendStatus
	 */
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	
	/**
	 * 读取属性：send_tmpl_cont
	 * @return
	 */
	public String getSendTmplCont() {
		return sendTmplCont;
	}
	
	/**
	 * 设置属性：send_tmpl_cont
	 * @param sendTmplCont
	 */
	public void setSendTmplCont(String sendTmplCont) {
		this.sendTmplCont = sendTmplCont;
	}
	
	/**
	 * 读取属性：send_tmpl_time
	 * @return
	 */
	public Date getSendTmplTime() {
		return (Date) sendTmplTime.clone();
	}
	
	/**
	 * 设置属性：send_tmpl_time
	 * @param sendTmplTime
	 */
	public void setSendTmplTime(Date sendTmplTime) {
		this.sendTmplTime = (Date) sendTmplTime.clone();
	}
	
	/**
	 * 读取属性：sender
	 * @return
	 */
	public String getSender() {
		return sender;
	}
	
	/**
	 * 设置属性：sender
	 * @param sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	/**
	 * 读取属性：tmpl_id
	 * @return
	 */
	public String getTmplId() {
		return tmplId;
	}
	
	/**
	 * 设置属性：tmpl_id
	 * @param tmplId
	 */
	public void setTmplId(String tmplId) {
		this.tmplId = tmplId;
	}
	
	/**
	 * 读取属性：touser
	 * @return
	 */
	public String getTouser() {
		return touser;
	}
	
	/**
	 * 设置属性：touser
	 * @param touser
	 */
	public void setTouser(String touser) {
		this.touser = touser;
	}
	
}
