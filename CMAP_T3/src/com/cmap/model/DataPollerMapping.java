package com.cmap.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name = "data_poller_mapping",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"mapping_id"})
		}
		)
public class DataPollerMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "mapping_id", unique = true)
	private String mappingId;

	@Column(name = "mapping_code", nullable = false)
	private String mappingCode;

	@Column(name = "is_source_column", nullable = false)
	private String isSourceColumn;

	@Column(name = "source_column_idx", nullable = true)
	private Integer sourceColumnIdx;

	@Column(name = "source_column_name", nullable = true)
	private String sourceColumnName;

	@Column(name = "source_column_type", nullable = true)
	private String sourceColumnType;

	@Column(name = "source_column_java_format", nullable = true)
	private String sourceColumnJavaFormat;

	@Column(name = "source_column_sql_format", nullable = true)
	private String sourceColumnSqlFormat;

	@Column(name = "target_table_name", nullable = false)
	private String targetTableName;

	@Column(name = "target_field_name", nullable = false)
	private String targetFieldName;

	@Column(name = "target_field_idx", nullable = false)
	private Integer targetFieldIdx;

	@Column(name = "target_field_type", nullable = true)
	private String targetFieldType;

	@Column(name = "target_value_format", nullable = true)
	private String targetValueFormat;

	@Column(name = "vo_field_name", nullable = true)
	private String voFieldName;

	@Column(name = "show_flag", nullable = false)
	private String showFlag;

	@Column(name = "create_time", nullable = false)
	private Timestamp createTime;

	@Column(name = "create_by", nullable = false)
	private String createBy;

	public DataPollerMapping() {
		super();
	}

	public DataPollerMapping(String mappingId, String mappingCode, String isSourceColumn, Integer sourceColumnIdx,
			String sourceColumnName, String sourceColumnType, String sourceColumnJavaFormat,
			String sourceColumnSqlFormat, String targetTableName, String targetFieldName, Integer targetFieldIdx,
			String targetFieldType, String targetValueFormat, String voFieldName, String showFlag, Timestamp createTime,
			String createBy) {
		super();
		this.mappingId = mappingId;
		this.mappingCode = mappingCode;
		this.isSourceColumn = isSourceColumn;
		this.sourceColumnIdx = sourceColumnIdx;
		this.sourceColumnName = sourceColumnName;
		this.sourceColumnType = sourceColumnType;
		this.sourceColumnJavaFormat = sourceColumnJavaFormat;
		this.sourceColumnSqlFormat = sourceColumnSqlFormat;
		this.targetTableName = targetTableName;
		this.targetFieldName = targetFieldName;
		this.targetFieldIdx = targetFieldIdx;
		this.targetFieldType = targetFieldType;
		this.targetValueFormat = targetValueFormat;
		this.voFieldName = voFieldName;
		this.showFlag = showFlag;
		this.createTime = createTime;
		this.createBy = createBy;
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	public String getMappingCode() {
		return mappingCode;
	}

	public void setMappingCode(String mappingCode) {
		this.mappingCode = mappingCode;
	}

	public String getIsSourceColumn() {
		return isSourceColumn;
	}

	public void setIsSourceColumn(String isSourceColumn) {
		this.isSourceColumn = isSourceColumn;
	}

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

	public String getVoFieldName() {
		return voFieldName;
	}

	public void setVoFieldName(String voFieldName) {
		this.voFieldName = voFieldName;
	}

	public String getShowFlag() {
		return showFlag;
	}

	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
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
}
