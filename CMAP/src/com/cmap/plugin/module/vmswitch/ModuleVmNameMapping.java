package com.cmap.plugin.module.vmswitch;

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
@Table(name="module_vm_name_mapping")
public class ModuleVmNameMapping implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapping_id", nullable = false)
	private Integer mappingId;

	@Column(name = "name_of_api", nullable = false)
	private String nameOfApi;

	@Column(name = "name_of_cmap", nullable = false)
	private String nameOfCmap;

	@Column(name = "device_list_id", nullable = false)
	private String deviceListId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "moduleVmNameMapping")
	private List<ModuleVmNameMappingDetail> ModuleVmNameMappingDetail;

	public ModuleVmNameMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ModuleVmNameMapping(Integer mappingId, String nameOfApi, String nameOfCmap, String deviceListId,
			List<com.cmap.plugin.module.vmswitch.ModuleVmNameMappingDetail> moduleVmNameMappingDetail) {
		super();
		this.mappingId = mappingId;
		this.nameOfApi = nameOfApi;
		this.nameOfCmap = nameOfCmap;
		this.deviceListId = deviceListId;
		ModuleVmNameMappingDetail = moduleVmNameMappingDetail;
	}

	public Integer getMappingId() {
		return mappingId;
	}

	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}

	public String getNameOfApi() {
		return nameOfApi;
	}

	public void setNameOfApi(String nameOfApi) {
		this.nameOfApi = nameOfApi;
	}

	public String getNameOfCmap() {
		return nameOfCmap;
	}

	public void setNameOfCmap(String nameOfCmap) {
		this.nameOfCmap = nameOfCmap;
	}

	public String getDeviceListId() {
		return deviceListId;
	}

	public void setDeviceListId(String deviceListId) {
		this.deviceListId = deviceListId;
	}

	public List<ModuleVmNameMappingDetail> getModuleVmNameMappingDetail() {
		return ModuleVmNameMappingDetail;
	}

	public void setModuleVmNameMappingDetail(List<ModuleVmNameMappingDetail> moduleVmNameMappingDetail) {
		ModuleVmNameMappingDetail = moduleVmNameMappingDetail;
	}
}
