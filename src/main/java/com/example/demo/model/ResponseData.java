package com.example.demo.model;

import java.util.HashMap;
import java.util.Map;

public class ResponseData {
	private int status;
	private String message;
	private Map<String, Object> data;

	
	public ResponseData(int status, String message, Map<String, Object> data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	public static ResponseData success(String message) {
		return new ResponseData(200, message, null);
	}
	public static ResponseData fail(String message) {
		return new ResponseData(400, message, null);
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public void put(String k,Object v) {
		if (this.data==null) {
			this.data = new HashMap<String, Object>();
		}
		data.put(k, v);
	}
}
