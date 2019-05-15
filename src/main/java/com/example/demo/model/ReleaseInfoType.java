package com.example.demo.model;

public enum ReleaseInfoType {
	rpm(1);
	private ReleaseInfoType(int type) {
		this.type=type;
	}
	private int type;
	public int getType() {
		return type;
	}
	
}
