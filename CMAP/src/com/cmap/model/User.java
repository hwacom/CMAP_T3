package com.cmap.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -5010954824778186468L;

	private String userName;

	private String password;

	private String passhash;

	private String ip;

	private String[] roles;

	public User() {
		super();
	}

	public User(String userName, String password, String passhash, String ip, String[] roles) {
		super();
		this.userName = userName;
		this.password = password;
		this.passhash = passhash;
		this.ip = ip;
		this.roles = roles;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
