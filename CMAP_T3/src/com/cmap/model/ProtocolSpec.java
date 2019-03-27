package com.cmap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
		name = "protocol_spec",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"protocol_no"})
		}
		)
public class ProtocolSpec implements java.io.Serializable {
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "PROTOCOL_NO", nullable = false)
	private Integer protocolNo;

	@Column(name = "HEX_NO", nullable = true)
	private String hexNo;

	@Column(name = "PROTOCOL_NAME", nullable = true)
	private String protocolName;

	@Column(name = "DESCRIPTION", nullable = true)
	private String description;

	@Column(name = "REFERENCE_DOC", nullable = true)
	private String referenceDoc;

	public ProtocolSpec() {
		super();
	}

	public ProtocolSpec(Integer protocolNo, String hexNo, String protocolName, String description,
			String referenceDoc) {
		super();
		this.protocolNo = protocolNo;
		this.hexNo = hexNo;
		this.protocolName = protocolName;
		this.description = description;
		this.referenceDoc = referenceDoc;
	}

	public Integer getProtocolNo() {
		return protocolNo;
	}

	public void setProtocolNo(Integer protocolNo) {
		this.protocolNo = protocolNo;
	}

	public String getHexNo() {
		return hexNo;
	}

	public void setHexNo(String hexNo) {
		this.hexNo = hexNo;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReferenceDoc() {
		return referenceDoc;
	}

	public void setReferenceDoc(String referenceDoc) {
		this.referenceDoc = referenceDoc;
	}
}
