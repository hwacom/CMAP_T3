package com.cmap.service.vo;

public class DataPollerServiceVO extends CommonServiceVO {

	private Integer sourceColumnIdx;
	private String sourceColumnName;
	private String sourceColumnType;
	private String sourceColumnJavaFormat;
	private String sourceColumnSqlFormat;
	private String targetTableName;
	private String targetFieldName;

	public Integer getSourceColumnIdx() {
		return sourceColumnIdx;
	}
	public void setSourceColumnIdx(Integer sourceColumnIdx) {
		this.sourceColumnIdx = sourceColumnIdx;
	}
	public String getSourceColumnName() {
		return sourceColumnName;
	}
	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}
	public String getSourceColumnType() {
		return sourceColumnType;
	}
	public void setSourceColumnType(String sourceColumnType) {
		this.sourceColumnType = sourceColumnType;
	}
	public String getSourceColumnJavaFormat() {
		return sourceColumnJavaFormat;
	}
	public void setSourceColumnJavaFormat(String sourceColumnJavaFormat) {
		this.sourceColumnJavaFormat = sourceColumnJavaFormat;
	}
	public String getSourceColumnSqlFormat() {
		return sourceColumnSqlFormat;
	}
	public void setSourceColumnSqlFormat(String sourceColumnSqlFormat) {
		this.sourceColumnSqlFormat = sourceColumnSqlFormat;
	}
	public String getTargetTableName() {
		return targetTableName;
	}
	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}
	public String getTargetFieldName() {
		return targetFieldName;
	}
	public void setTargetFieldName(String targetFieldName) {
		this.targetFieldName = targetFieldName;
	}
}
