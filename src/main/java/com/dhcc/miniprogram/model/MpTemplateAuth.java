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
 * 小程序订阅消息模板授权-POJO
 * @author cb
 * @since 2020-07-04
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate(true)
@Proxy(lazy = false)
@Table(name = "mp_template_auth", schema="dhcplat_zjzwfwzx", catalog="dhcplat_zjzwfwzx")
@TableDefine(title = "小程序订阅消息模板授权")
public class MpTemplateAuth extends IdentifiedPo implements Serializable {
	private static final long serialVersionUID = 1L;

	@FieldDefine(title = "手机号", number = 2)
	@Column(name = "phone", length = 11, unique = false, nullable = true)
	private String phone;

	@FieldDefine(title = "模板ID", number = 3)
	@Column(name = "template_id", length = 50, unique = false, nullable = true)
	private String templateId;

	@FieldDefine(title = "模板标题", number = 4)
	@Column(name = "title", length = 32, unique = false, nullable = true)
	private String title;

	@FieldDefine(title = "描述", number = 5)
	@Column(name = "tmpl_desc", length = 200, unique = false, nullable = true)
	private String tmplDesc;

	@FieldDefine(title = "模板类型：2-一次性消息、3-长期消息", number = 6)
	@Column(name = "type", length = 3, unique = false, nullable = true)
	private Integer type;

	@FieldDefine(title = "模板排序,按从小到大的顺序排版", number = 7)
	@Column(name = "tmpl_order", length = 3, unique = false, nullable = true)
	private Integer tmplOrder;

	@FieldDefine(title = "是否订阅: 1-订阅、2-取消、3-禁止", number = 8)
	@Column(name = "is_sub", length = 3, unique = false, nullable = true)
	private Integer isSub;

	@FieldDefine(title = "创建人", number = 9)
	@Column(name = "create_user", length = 30, unique = false, nullable = true)
	private String createUser;

	@FieldDefine(title = "创建时间", number = 10)
	@Column(name = "create_time", length = 26, unique = false, nullable = true)
	private Date createTime;

	@FieldDefine(title = "修改人", number = 11)
	@Column(name = "modify_user", length = 30, unique = false, nullable = true)
	private String modifyUser;

	@FieldDefine(title = "修改时间", number = 12)
	@Column(name = "modify_time", length = 26, unique = false, nullable = true)
	private Date modifyTime;

	public MpTemplateAuth() {

	}

	/**
	 * 读取属性：手机号
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置属性：手机号
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 读取属性：模板ID
	 * @return
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * 设置属性：模板ID
	 * @param templateId
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * 读取属性：模板标题
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置属性：模板标题
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 读取属性：描述
	 * @return
	 */
	public String getTmplDesc() {
		return tmplDesc;
	}

	/**
	 * 设置属性：描述
	 * @param tmplDesc
	 */
	public void setTmplDesc(String tmplDesc) {
		this.tmplDesc = tmplDesc;
	}

	/**
	 * 读取属性：模板类型：2-一次性消息、3-长期消息
	 * @return
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置属性：模板类型：2-一次性消息、3-长期消息
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 读取属性：模板排序,按从小到大的顺序排版
	 * @return
	 */
	public Integer getTmplOrder() {
		return tmplOrder;
	}

	/**
	 * 设置属性：模板排序,按从小到大的顺序排版
	 * @param tmplOrder
	 */
	public void setTmplOrder(Integer tmplOrder) {
		this.tmplOrder = tmplOrder;
	}

	/**
	 * 读取属性：是否订阅: 1-订阅、2-取消、3-禁止
	 * @return
	 */
	public Integer getIsSub() {
		return isSub;
	}

	/**
	 * 设置属性：是否订阅: 1-订阅、2-取消、3-禁止
	 * @param isSub
	 */
	public void setIsSub(Integer isSub) {
		this.isSub = isSub;
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
