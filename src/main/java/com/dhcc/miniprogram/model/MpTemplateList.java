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
import com.dhcc.basic.model.AuditedPo;
import com.dhcc.basic.model.IdentifiedPo;

/**
 * 小程序模板列表-POJO
 * @author cb
 * @since 2020-07-03
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate(true)
@Proxy(lazy = false)
@Table(name = "mp_template_list", schema="dhcplat", catalog="dhcplat")
@TableDefine(title = "小程序模板列表")
public class MpTemplateList extends IdentifiedPo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@FieldDefine(title = "添加至帐号下的模板 id，发送小程序订阅消息时所需", number = 2)
	@Column(name = "template_id", length = 50, unique = false, nullable = true)
	private String templateId;
	
	@FieldDefine(title = "模版标题", number = 3)
	@Column(name = "title", length = 25, unique = false, nullable = true)
	private String title;
	
	@FieldDefine(title = "模版内容", number = 4)
	@Column(name = "content", length = 255, unique = false, nullable = true)
	private String content;
	
	@FieldDefine(title = "模板内容示例", number = 5)
	@Column(name = "example", length = 255, unique = false, nullable = true)
	private String example;
	
	@FieldDefine(title = "模版类型：2 为一次性订阅，3 为长期订阅", number = 6)
	@Column(name = "type", length = 3, unique = false, nullable = true)
	private Integer type;
	
	@FieldDefine(title = "创建时间", number = 7)
	@Column(name = "create_time", length = 26, unique = false, nullable = true)
	private Date createTime;
	
	public MpTemplateList() {
		
	}
	
	/**
	 * 读取属性：添加至帐号下的模板 id，发送小程序订阅消息时所需
	 * @return
	 */
	public String getTemplateId() {
		return templateId;
	}
	
	/**
	 * 设置属性：添加至帐号下的模板 id，发送小程序订阅消息时所需
	 * @param templateId
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	/**
	 * 读取属性：模版标题
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * 设置属性：模版标题
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 读取属性：模版内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * 设置属性：模版内容
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * 读取属性：模板内容示例
	 * @return
	 */
	public String getExample() {
		return example;
	}
	
	/**
	 * 设置属性：模板内容示例
	 * @param example
	 */
	public void setExample(String example) {
		this.example = example;
	}
	
	/**
	 * 读取属性：模版类型：2 为一次性订阅，3 为长期订阅
	 * @return
	 */
	public Integer getType() {
		return type;
	}
	
	/**
	 * 设置属性：模版类型：2 为一次性订阅，3 为长期订阅
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
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
