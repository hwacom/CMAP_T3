package com.cmap.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -5010954824778186468L;

	private String userName;

	private String userChineseName;

	private String userUnit;

	private String email;

	private String prtgLoginAccount;

	private String prtgLoginPassword;

	private String oidcSub;

	private String password;

	private String passhash;

	private String ip;

	private String schoolId;

	private String[] roles;

	public User() {
		super();
	}

	public User(String userName, String userChineseName, String userUnit, String email, String prtgLoginAccount,
			String prtgLoginPassword, String oidcSub, String password, String passhash, String ip, String schoolId,
			String[] roles) {
		super();
		this.userName = userName;
		this.userChineseName = userChineseName;
		this.userUnit = userUnit;
		this.email = email;
		this.prtgLoginAccount = prtgLoginAccount;
		this.prtgLoginPassword = prtgLoginPassword;
		this.oidcSub = oidcSub;
		this.password = password;
		this.passhash = passhash;
		this.ip = ip;
		this.schoolId = schoolId;
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserChineseName() {
		return userChineseName;
	}

	public void setUserChineseName(String userChineseName) {
		this.userChineseName = userChineseName;
	}

	public String getUserUnit() {
		return userUnit;
	}

	public void setUserUnit(String userUnit) {
		this.userUnit = userUnit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPrtgLoginAccount() {
		return prtgLoginAccount;
	}

	public void setPrtgLoginAccount(String prtgLoginAccount) {
		this.prtgLoginAccount = prtgLoginAccount;
	}

	public String getPrtgLoginPassword() {
		return prtgLoginPassword;
	}

	public void setPrtgLoginPassword(String prtgLoginPassword) {
		this.prtgLoginPassword = prtgLoginPassword;
	}

	public String getOidcSub() {
		return oidcSub;
	}

	public void setOidcSub(String oidcSub) {
		this.oidcSub = oidcSub;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasshash() {
		return passhash;
	}

	public void setPasshash(String passhash) {
		this.passhash = passhash;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
