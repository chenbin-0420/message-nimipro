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
 * 群发日志表-POJO
 * @author cb
 * @since 2020-08-02
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate(true)
@Proxy(lazy = false)
@Table(name = "mp_mass_journal", schema="dhcplat_zjzwfwzx", catalog="dhcplat_zjzwfwzx")
@TableDefine(title = "群发日志表")
public class MpMassJournal extends IdentifiedPo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@FieldDefine(title = "要求的发送手机号", number = 2)
	@Column(name = "require_content", length = 12010, unique = false, nullable = true)
	private String requireContent;
	
	@FieldDefine(title = "要求的发送总数", number = 3)
	@Column(name = "require_count", length = 4, unique = false, nullable = true)
	private Integer requireCount;

	@FieldDefine(title = "有效的总数", number = 4)
	@Column(name = "valid_count", length = 4, unique = false, nullable = true)
	private Integer validCount;

	@FieldDefine(title = "成功的总数", number = 5)
	@Column(name = "success_count", length = 4, unique = false, nullable = true)
	private Integer successCount;

	@FieldDefine(title = "失败的总数", number = 6)
	@Column(name = "failure_count", length = 4, unique = false, nullable = true)
	private Integer failureCount;

	@FieldDefine(title = "创建时间", number = 7)
	@Column(name = "create_time", length = 26, unique = false, nullable = true)
	private Date createTime;

	@FieldDefine(title = "修改时间", number = 8)
	@Column(name = "modify_time", length = 26, unique = false, nullable = true)
	private Date modifyTime;

	public MpMassJournal() {

	}

	/**
	 * 读取属性：要求的发送手机号
	 * @return
	 */
	public String getRequireContent() {
		return requireContent;
	}

	/**
	 * 设置属性：要求的发送手机号
	 * @param requireContent
	 */
	public void setRequireContent(String requireContent) {
		this.requireContent = requireContent;
	}

	/**
	 * 读取属性：要求的发送总数
	 * @return
	 */
	public Integer getRequireCount() {
		return requireCount;
	}

	/**
	 * 设置属性：要求的发送总数
	 * @param requireCount
	 */
	public void setRequireCount(Integer requireCount) {
		this.requireCount = requireCount;
	}
	
	/**
	 * 读取属性：有效的总数
	 * @return
	 */
	public Integer getValidCount() {
		return validCount;
	}
	
	/**
	 * 设置属性：有效的总数
	 * @param validCount
	 */
	public void setValidCount(Integer validCount) {
		this.validCount = validCount;
	}
	
	/**
	 * 读取属性：成功的总数
	 * @return
	 */
	public Integer getSuccessCount() {
		return successCount;
	}
	
	/**
	 * 设置属性：成功的总数
	 * @param successCount
	 */
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	
	/**
	 * 读取属性：失败的总数
	 * @return
	 */
	public Integer getFailureCount() {
		return failureCount;
	}
	
	/**
	 * 设置属性：失败的总数
	 * @param failureCount
	 */
	public void setFailureCount(Integer failureCount) {
		this.failureCount = failureCount;
	}
	
	/**
	 * 读取属性：创建时间
	 * @return
	 */
	public Date getCreateTime() {
		return createTime != null ? (Date) createTime.clone() : null;
	}
	
	/**
	 * 设置属性：创建时间
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 读取属性：修改时间
	 * @return
	 */
	public Date getModifyTime() {
		return modifyTime != null ? (Date) modifyTime.clone() : null;
	}
	
	/**
	 * 设置属性：修改时间
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
