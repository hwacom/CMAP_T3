package com.cmap.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="i18n")
public class I18n implements java.io.Serializable {
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name = "I18N_ID", nullable = false)
	private String id;
	

	@Column(name = "I18N_KEY", nullable = false)
	private String key;
	

	@Column(name = "I18N_VALUE", nullable = false)
	private String value;
	
	@Column(name = "LOCALE_COUNTRY", nullable = false)
	private String locale;
	
	public I18n() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public I18n(String id, String key, String value, String locale) {
		super();
		this.id = id;
		this.key = key;
		this.value = value;
		this.locale = locale;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}


	

}
