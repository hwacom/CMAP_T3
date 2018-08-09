package com.cmap.model;

public class User {

	private String userName;
	
	private String password;
	
	private String passhash;
	
	private String[] roles;

	public User() {
		super();
	}

	public User(String userName, String password, String passhash, String[] roles) {
		super();
		this.userName = userName;
		this.password = password;
		this.passhash = passhash;
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

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}
}
