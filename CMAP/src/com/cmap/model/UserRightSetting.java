package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cmap.Constants;

@Entity
@Table(
	name = "user_right_setting",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"ID"})
	}
)
public class UserRightSetting implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true)
	private String id;

	@Column(name = "ACCOUNT", nullable = false)
	private String account;

	@Column(name = "IS_ADMIN", nullable = false)
	private String isAdmin = Constants.DATA_N;

	@Column(name = "DENY_ACCESS", nullable = false)
	private String denyAccess = Constants.DATA_N;

	@Column(name = "SOURCE_IP_ADDR", nullable = false)
	private String sourceIpAddr = Constants.DATA_STAR_SYMBOL;

	@Column(name = "USER_GROUP", nullable = true)
	private String userGroup;

	@Column(name = "DELETE_FLAG", nullable = false)
	private String deleteFlag;

	@Column(name = "DELETE_TIME", nullable = true)
	private Timestamp deleteTime;

	@Column(name = "DELETE_BY", nullable = true)
	private String deleteBy;

	@Column(name = "CREATE_TIME", nullable = true)
	private Timestamp createTime;

	@Column(name = "CREATE_BY", nullable = true)
	private String createBy;

	@Column(name = "UPDATE_TIME", nullable = true)
	private Timestamp updateTime;

	@Column(name = "UPDATE_BY", nullable = true)
	private String updateBy;

	public UserRightSetting() {
		super();
	}

	public UserRightSetting(String id, String account, String isAdmin, String denyAccess, String sourceIpAddr,
			String userGroup, String deleteFlag, Timestamp deleteTime, String deleteBy, Timestamp createTime,
			String createBy, Timestamp updateTime, String updateBy) {
		super();
		this.id = id;
		this.account = account;
		this.isAdmin = isAdmin;
		this.denyAccess = denyAccess;
		this.sourceIpAddr = sourceIpAddr;
		this.userGroup = userGroup;
		this.deleteFlag = deleteFlag;
		this.deleteTime = deleteTime;
		this.deleteBy = deleteBy;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getDenyAccess() {
		return denyAccess;
	}

	public void setDenyAccess(String denyAccess) {
		this.denyAccess = denyAccess;
	}

	public String getSourceIpAddr() {
		return sourceIpAddr;
	}

	public void setSourceIpAddr(String sourceIpAddr) {
		this.sourceIpAddr = sourceIpAddr;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
