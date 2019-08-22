package com.cmap.service.vo;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DataPollerServiceVO extends CommonServiceVO {

	private String isSourceColumn;
	private Integer sourceColumnIdx;
	private String sourceColumnName;
	private String sourceColumnType;
	private String sourceColumnJavaFormat;
	private String sourceColumnSqlFormat;
	private String targetTableName;
	private String targetFieldName;
	private Integer targetFieldIdx;
	private String targetFieldType;
	private String targetValueFormat;
	private String voFieldName;

	private File targetFile;

	private String retTableName;
	private String retOriFileName;

	private List<Map<String, String>> sourceEntryMapList;

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
	public Integer getTargetFieldIdx() {
		return targetFieldIdx;
	}
	public void setTargetFieldIdx(Integer targetFieldIdx) {
		this.targetFieldIdx = targetFieldIdx;
	}
	public String getIsSourceColumn() {
		return isSourceColumn;
	}
	public void setIsSourceColumn(String isSourceColumn) {
		this.isSourceColumn = isSourceColumn;
	}
	public String getTargetFieldType() {
		return targetFieldType;
	}
	public void setTargetFieldType(String targetFieldType) {
		this.targetFieldType = targetFieldType;
	}
	public String getTargetValueFormat() {
		return targetValueFormat;
	}
	public void setTargetValueFormat(String targetValueFormat) {
		this.targetValueFormat = targetValueFormat;
	}
	public String getRetTableName() {
		return retTableName;
	}
	public void setRetTableName(String retTableName) {
		this.retTableName = retTableName;
	}
	public String getRetOriFileName() {
		return retOriFileName;
	}
	public void setRetOriFileName(String retOriFileName) {
		this.retOriFileName = retOriFileName;
	}
	public String getVoFieldName() {
		return voFieldName;
	}
	public void setVoFieldName(String voFieldName) {
		this.voFieldName = voFieldName;
	}
	public List<Map<String, String>> getSourceEntryMapList() {
		return sourceEntryMapList;
	}
	public void setSourceEntryMapList(List<Map<String, String>> sourceEntryMapList) {
		this.sourceEntryMapList = sourceEntryMapList;
	}
    public File getTargetFile() {
        return targetFile;
    }
    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }
}
