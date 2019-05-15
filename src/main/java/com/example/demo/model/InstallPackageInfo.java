package com.example.demo.model;

public class InstallPackageInfo {
	private String packageName;
	private String version;
	private String fullName;
	private String  lastModified;
	private String size;
	private String description;
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public static String parsePackageName(String fullName) {
		if (fullName== null) return null;
		/**fullName:  xylink-vulture-hankos-debug-0.1.7-176.x86_64.rpm
		   Name: xylink-vulture-hankos-debug
           Release: 215  
           Version: 0.1.7                            
		 * packageName: xylink-vulture-hankos-debug-0.1.7-176
		 * */
		int i = fullName.lastIndexOf("-");
		String s = fullName.substring(i);//176.x86_64.rpm
		String release = s.substring(0,s.indexOf("."));//176
		return fullName.substring(0, i)+release;//xylink-vulture-hankos-debug-0.1.7-176
	}
	public static String parseVersion(String fullName) {
		if (fullName== null) return null;
		int i = fullName.lastIndexOf("-");
		String s1 = fullName.substring(i);//176.x86_64.rpm
		String s2 = fullName.substring(0, i);//xylink-vulture-hankos-debug-0.1.7
		String release = s1.substring(0,s1.indexOf("."));//176
		String version=s2.substring(s2.lastIndexOf("-")+1);
		return version+release;
	}
}
