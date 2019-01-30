package com.cmap.dao.vo;

import java.util.List;

public class ConfigVersionInfoDAOVO extends CommonDAOVO {

	private String queryVersionId;
	private String queryGroup1;
	private String queryGroup2;
	private String queryDevice1;
	private String queryDevice2;
	private String queryDateBegin1;
	private String queryDateEnd1;
	private String queryDateBegin2;
	private String queryDateEnd2;
	private String queryConfigType;
	private String queryDeviceListId;

	private boolean jobTrigger = false;

	private List<String> queryGroup1List;
	private List<String> queryGroup2List;

	private List<String> queryDevice1List;
	private List<String> queryDevice2List;

	public String getQueryGroup1() {
		return queryGroup1;
	}
	public void setQueryGroup1(String queryGroup1) {
		this.queryGroup1 = queryGroup1;
	}
	public String getQueryGroup2() {
		return queryGroup2;
	}
	public void setQueryGroup2(String queryGroup2) {
		this.queryGroup2 = queryGroup2;
	}
	public String getQueryDevice1() {
		return queryDevice1;
	}
	public void setQueryDevice1(String queryDevice1) {
		this.queryDevice1 = queryDevice1;
	}
	public String getQueryDevice2() {
		return queryDevice2;
	}
	public void setQueryDevice2(String queryDevice2) {
		this.queryDevice2 = queryDevice2;
	}
	public String getQueryDateBegin1() {
		return queryDateBegin1;
	}
	public void setQueryDateBegin1(String queryDateBegin1) {
		this.queryDateBegin1 = queryDateBegin1;
	}
	public String getQueryDateEnd1() {
		return queryDateEnd1;
	}
	public void setQueryDateEnd1(String queryDateEnd1) {
		this.queryDateEnd1 = queryDateEnd1;
	}
	public String getQueryDateBegin2() {
		return queryDateBegin2;
	}
	public void setQueryDateBegin2(String queryDateBegin2) {
		this.queryDateBegin2 = queryDateBegin2;
	}
	public String getQueryDateEnd2() {
		return queryDateEnd2;
	}
	public void setQueryDateEnd2(String queryDateEnd2) {
		this.queryDateEnd2 = queryDateEnd2;
	}
	public String getQueryConfigType() {
		return queryConfigType;
	}
	public void setQueryConfigType(String queryConfigType) {
		this.queryConfigType = queryConfigType;
	}
	public List<String> getQueryGroup1List() {
		return queryGroup1List;
	}
	public void setQueryGroup1List(List<String> queryGroup1List) {
		this.queryGroup1List = queryGroup1List;
	}
	public List<String> getQueryGroup2List() {
		return queryGroup2List;
	}
	public void setQueryGroup2List(List<String> queryGroup2List) {
		this.queryGroup2List = queryGroup2List;
	}
	public List<String> getQueryDevice1List() {
		return queryDevice1List;
	}
	public void setQueryDevice1List(List<String> queryDevice1List) {
		this.queryDevice1List = queryDevice1List;
	}
	public List<String> getQueryDevice2List() {
		return queryDevice2List;
	}
	public void setQueryDevice2List(List<String> queryDevice2List) {
		this.queryDevice2List = queryDevice2List;
	}
	public boolean isJobTrigger() {
		return jobTrigger;
	}
	public void setJobTrigger(boolean jobTrigger) {
		this.jobTrigger = jobTrigger;
	}
	public String getQueryVersionId() {
		return queryVersionId;
	}
	public void setQueryVersionId(String queryVersionId) {
		this.queryVersionId = queryVersionId;
	}
	public String getQueryDeviceListId() {
		return queryDeviceListId;
	}
	public void setQueryDeviceListId(String queryDeviceListId) {
		this.queryDeviceListId = queryDeviceListId;
	}
}
