package com.example.demo.model;

import java.beans.Transient;


public class ReleaseInfoConfig{
    protected String id;
	private String name;
	/** 包名格式 */
	private String pattern;
	/** 1-rpm */
	private int type;
	/** rpm安装包路径 */
	private String dir;

	//备用字段 
	/** 终端platform */
	private String platform;
	/** 终端对应的json文件 */
	private String json;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
	
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	@Transient
	public String parseInstallPackage(String version){
		if (this.pattern == null) return null;
		if (this.type ==ReleaseInfoType.rpm.getType()) {
			return this.pattern.replace("{version}", version);
		}
		return null;
	}
	@Transient
	public String getInstallPackagePrefix(){
		if (this.pattern == null) return null;
		if (this.type ==ReleaseInfoType.rpm.getType()) {
			return this.pattern.replace("{version}", "");
		}
		return null;
	}
	@Transient
	public static String parseVersion(String packagaName){
		if (packagaName == null) return null;
		int lastIndexOf = packagaName.lastIndexOf("-");
		String release= packagaName.substring(lastIndexOf);
		String s = packagaName.substring(0,lastIndexOf);
		String version = s.substring(s.lastIndexOf("-")+1);
		return version+release;
	}

}
