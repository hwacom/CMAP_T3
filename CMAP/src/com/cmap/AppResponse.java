package com.cmap;

import java.util.HashMap;
import java.util.Map;

public class AppResponse {

	public int code;
	public String message;
	public Map<String, Object> data = new HashMap<String, Object>();
	
	public AppResponse(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public AppResponse(int code, String message, HashMap<String, Object> data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public AppResponse putData(String key, Object value) {
		this.data.put(key, value);
		return this;
	}
}
