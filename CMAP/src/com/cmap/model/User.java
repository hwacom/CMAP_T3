package com.cmap.model;

public class User {

	private String userName;
	
	private String password;
	
	private String passhash;
	
	private String role;

	public User() {
		super();
	}

	public User(String userName, String password, String passhash, String role) {
		super();
		this.userName = userName;
		this.password = password;
		this.passhash = passhash;
		this.role = role;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
