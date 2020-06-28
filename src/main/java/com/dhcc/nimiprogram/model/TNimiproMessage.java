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
 * 小程序模板消息-POJO
 * @author cb
 * @since 2020-06-19
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate(true)
@Proxy(lazy = false)
@Table(name = "t_nimipro_message", schema="dhcplat", catalog="dhcplat")
@TableDefine(title = "小程序模板消息")
public class TNimiproMessage extends IdentifiedPo implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldDefine(title = "小程序ID", number = 2)
	@Column(name = "app_id", length = 32, unique = false, nullable = true)
	private String appId;

	@FieldDefine(title = "接收者（用户）的openid", number = 3)
	@Column(name = "touser", length = 30, unique = false, nullable = true)
	private String touser;

	@FieldDefine(title = "模板ID", number = 4)
	@Column(name = "tmpl_id", length = 50, unique = false, nullable = true)
	private String tmplId;

	@FieldDefine(title = "跳转页面：点击模板卡片后的跳转页面，仅限本小程序内的页面。", number = 5)
	@Column(name = "jump_page", length = 255, unique = false, nullable = true)
	private String jumpPage;

	@FieldDefine(title = "跳转小程序类型：developer-开发版、trial-体验版、formal-正式版、默认为正式版", number = 6)
	@Column(name = "nimipro_state", length = 10, unique = false, nullable = true)
	private String nimiproState;

	@FieldDefine(title = "语言类型", number = 7)
	@Column(name = "lang_type", length = 5, unique = false, nullable = true)
	private String langType;

	@FieldDefine(title = "发送模板内容", number = 8)
	@Column(name = "send_tmpl_cont", length = 500, unique = false, nullable = true)
	private String sendTmplCont;

	@FieldDefine(title = "发送模板时间", number = 9)
	@Column(name = "send_tmpl_time", length = 26, unique = false, nullable = true)
	private Date sendTmplTime;

	@FieldDefine(title = "发送者", number = 10)
	@Column(name = "sender", length = 32, unique = false, nullable = true)
	private String sender;

	@FieldDefine(title = "消息渠道:SQ-社区、YQ-园区", number = 11)
	@Column(name = "channel", length = 2, unique = false, nullable = true)
	private String channel;

	@FieldDefine(title = "发送状态：CG-成功、SB-失败", number = 12)
	@Column(name = "send_status", length = 2, unique = false, nullable = true)
	private String sendStatus;

	@FieldDefine(title = "创建时间", number = 13)
	@Column(name = "create_time", length = 26, unique = false, nullable = true)
	private Date createTime;

	public TNimiproMessage() {

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
	 * 读取属性：接收者（用户）的openid
	 * @return
	 */
	public String getTouser() {
		return touser;
	}

	/**
	 * 设置属性：接收者（用户）的openid
	 * @param touser
	 */
	public void setTouser(String touser) {
		this.touser = touser;
	}

	/**
	 * 读取属性：模板ID
	 * @return
	 */
	public String getTmplId() {
		return tmplId;
	}

	/**
	 * 设置属性：模板ID
	 * @param tmplId
	 */
	public void setTmplId(String tmplId) {
		this.tmplId = tmplId;
	}

	/**
	 * 读取属性：跳转页面：点击模板卡片后的跳转页面，仅限本小程序内的页面。
	 * @return
	 */
	public String getJumpPage() {
		return jumpPage;
	}

	/**
	 * 设置属性：跳转页面：点击模板卡片后的跳转页面，仅限本小程序内的页面。
	 * @param jumpPage
	 */
	public void setJumpPage(String jumpPage) {
		this.jumpPage = jumpPage;
	}

	/**
	 * 读取属性：跳转小程序类型：developer-开发版、trial-体验版、formal-正式版、默认为正式版
	 * @return
	 */
	public String getNimiproState() {
		return nimiproState;
	}

	/**
	 * 设置属性：跳转小程序类型：developer-开发版、trial-体验版、formal-正式版、默认为正式版
	 * @param nimiproState
	 */
	public void setNimiproState(String nimiproState) {
		this.nimiproState = nimiproState;
	}

	/**
	 * 读取属性：语言类型
	 * @return
	 */
	public String getLangType() {
		return langType;
	}

	/**
	 * 设置属性：语言类型
	 * @param langType
	 */
	public void setLangType(String langType) {
		this.langType = langType;
	}

	/**
	 * 读取属性：发送模板内容
	 * @return
	 */
	public String getSendTmplCont() {
		return sendTmplCont;
	}

	/**
	 * 设置属性：发送模板内容
	 * @param sendTmplCont
	 */
	public void setSendTmplCont(String sendTmplCont) {
		this.sendTmplCont = sendTmplCont;
	}

	/**
	 * 读取属性：发送模板时间
	 * @return
	 */
	public Date getSendTmplTime() {
		return (Date) sendTmplTime.clone();
	}

	/**
	 * 设置属性：发送模板时间
	 * @param sendTmplTime
	 */
	public void setSendTmplTime(Date sendTmplTime) {
		this.sendTmplTime = (Date) sendTmplTime.clone();
	}

	/**
	 * 读取属性：发送者
	 * @return
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * 设置属性：发送者
	 * @param sender
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * 读取属性：消息渠道:SQ-社区、YQ-园区
	 * @return
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * 设置属性：消息渠道:SQ-社区、YQ-园区
	 * @param channel
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * 读取属性：发送状态：CG-成功、SB-失败
	 * @return
	 */
	public String getSendStatus() {
		return sendStatus;
	}

	/**
	 * 设置属性：发送状态：CG-成功、SB-失败
	 * @param sendStatus
	 */
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
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
}