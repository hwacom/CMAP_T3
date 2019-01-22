package com.cmap.plugin.module.vmswitch;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="module_vm_esxi_setting")
public class ModuleVmEsxiSetting implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "setting_id", nullable = false)
	private Integer settingId;

	@Column(name = "esxi_name", nullable = false)
	private String esxiName;

	@Column(name = "host_ip", nullable = false)
	private String hostIp;

	@Column(name = "login_account", nullable = false)
	private String loginAccount;

	@Column(name = "login_password", nullable = false)
	private String loginPassword;

	@Column(name = "create_time", nullable = true)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = true)
	private String createBy;

	@Column(name = "update_time", nullable = true)
	private Timestamp updateTime;

	@Column(name = "update_by", nullable = true)
	private String updateBy;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "moduleVmEsxiSetting")
	private List<ModuleVmNameMappingDetail> ModuleVmNameMappingDetail;

	public ModuleVmEsxiSetting() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ModuleVmEsxiSetting(Integer settingId, String esxiName, String hostIp, String loginAccount,
			String loginPassword, Timestamp createTime, String createBy, Timestamp updateTime, String updateBy,
			List<com.cmap.plugin.module.vmswitch.ModuleVmNameMappingDetail> moduleVmNameMappingDetail) {
		super();
		this.settingId = settingId;
		this.esxiName = esxiName;
		this.hostIp = hostIp;
		this.loginAccount = loginAccount;
		this.loginPassword = loginPassword;
		this.createTime = createTime;
		this.createBy = createBy;
		this.updateTime = updateTime;
		this.updateBy = updateBy;
		ModuleVmNameMappingDetail = moduleVmNameMappingDetail;
	}

	public Integer getSettingId() {
		return settingId;
	}

	public void setSettingId(Integer settingId) {
		this.settingId = settingId;
	}

	public String getEsxiName() {
		return esxiName;
	}

	public void setEsxiName(String esxiName) {
		this.esxiName = esxiName;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
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

	public List<ModuleVmNameMappingDetail> getModuleVmNameMappingDetail() {
		return ModuleVmNameMappingDetail;
	}

	public void setModuleVmNameMappingDetail(List<ModuleVmNameMappingDetail> moduleVmNameMappingDetail) {
		ModuleVmNameMappingDetail = moduleVmNameMappingDetail;
	}
}
