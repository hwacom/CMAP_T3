package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(
	name = "menu_item",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"menu_item_id"})
	}
)
public class MenuItem {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "menu_item_id", unique = true)
	private String menuItemId;
	
	@Column(name = "menu_code", nullable = false)
	private String menuCode;
	
	@Column(name = "option_label", nullable = false)
	private String optionLabel;
	
	@Column(name = "option_value", nullable = false)
	private String optionValue;
	
	@Column(name = "option_order", nullable = false)
	private Integer optionOrder;
	
	@Column(name = "remark", nullable = true)
	private String remark;
	
	@Column(name = "delete_flag", nullable = false)
	private String deleteFlag;
	
	@Column(name = "delete_time", nullable = true)
	private Timestamp deleteTime;
	
	@Column(name = "delete_by", nullable = true)
	private String deleteBy;
	
	@Column(name = "create_time", nullable = true)
	private Timestamp createTime;
	
	@Column(name = "create_by", nullable = true)
	private String createBy;
	
	@Column(name = "update_time", nullable = true)
	private Timestamp updateTime;
	
	@Column(name = "update_by", nullable = true)
	private String updateBy;
	
	public MenuItem() {
		super();
	}

	public MenuItem(String menuItemId, String menuCode, String optionLabel, String optionValue, Integer optionOrder,
			String remark, String deleteFlag, Timestamp deleteTime, String deleteBy, Timestamp createTime,
			String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.menuItemId = menuItemId;
		this.menuCode = menuCode;
		this.optionLabel = optionLabel;
		this.optionValue = optionValue;
		this.optionOrder = optionOrder;
		this.remark = remark;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(String menuItemId) {
		this.menuItemId = menuItemId;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getOptionLabel() {
		return optionLabel;
	}

	public void setOptionLabel(String optionLabel) {
		this.optionLabel = optionLabel;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public Integer getOptionOrder() {
		return optionOrder;
	}

	public void setOptionOrder(Integer optionOrder) {
		this.optionOrder = optionOrder;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Timestamp getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getDeleteBy() {
		return deleteBy;
	}

	public void setDeleteBy(String deleteBy) {
		this.deleteBy = deleteBy;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
}
