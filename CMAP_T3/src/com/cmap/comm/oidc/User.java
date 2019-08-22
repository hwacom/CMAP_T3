package com.cmap.comm.oidc;

public class User {
	private String id;
	private String name;
	private String role;
	private String title;
    private String[] groups;
    
    public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String[] getGroups() {
		return groups;
	}
	
	public void setGroups(String[] groups) {
		this.groups = groups;
	}
}
