package com.cmap.plugin.module.vmswitch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="module_vm_name_mapping_detail")
public class ModuleVmNameMappingDetail implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapping_detail_id", nullable = false)
	private Integer mappingDetailId;

	@Column(name = "name_of_vmware", nullable = false)
	private String nameOfVmware;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "mapping_id", nullable = false)
	private ModuleVmNameMapping moduleVmNameMapping;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "esxi_setting_id", nullable = false)
	private ModuleVmEsxiSetting moduleVmEsxiSetting;

	public ModuleVmNameMappingDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ModuleVmNameMappingDetail(Integer mappingDetailId, String nameOfVmware,
			ModuleVmNameMapping moduleVmNameMapping, ModuleVmEsxiSetting moduleVmEsxiSetting) {
		super();
		this.mappingDetailId = mappingDetailId;
		this.nameOfVmware = nameOfVmware;
		this.moduleVmNameMapping = moduleVmNameMapping;
		this.moduleVmEsxiSetting = moduleVmEsxiSetting;
	}

	public Integer getMappingDetailId() {
		return mappingDetailId;
	}

	public void setMappingDetailId(Integer mappingDetailId) {
		this.mappingDetailId = mappingDetailId;
	}

	public String getNameOfVmware() {
		return nameOfVmware;
	}

	public void setNameOfVmware(String nameOfVmware) {
		this.nameOfVmware = nameOfVmware;
	}

	public ModuleVmNameMapping getModuleVmNameMapping() {
		return moduleVmNameMapping;
	}

	public void setModuleVmNameMapping(ModuleVmNameMapping moduleVmNameMapping) {
		this.moduleVmNameMapping = moduleVmNameMapping;
	}

	public ModuleVmEsxiSetting getModuleVmEsxiSetting() {
		return moduleVmEsxiSetting;
	}

	public void setModuleVmEsxiSetting(ModuleVmEsxiSetting moduleVmEsxiSetting) {
		this.moduleVmEsxiSetting = moduleVmEsxiSetting;
	}
}
