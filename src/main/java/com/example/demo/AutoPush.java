package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.InstallPackageInfo;
import com.example.demo.model.ReleaseInfoConfig;
import com.example.demo.model.ReleaseInfoType;
import com.example.demo.model.ResponseData;

@RestController
@RequestMapping("/api/rest")
public class AutoPush {
	Logger logger=LoggerFactory.getLogger(AutoPush.class);
	private static final String SHELL_PARAM_PACKAGE="-r";
	private static final String SHELL_PARAM_VERSION="-v";
	private static final String SHELL_PARAM_PLATFORM="-p";
	private static final String SHELL_PARAM_DIR="-d";
	
	private String RPM_SHELL="/usr/bin/zl.sh";
	
	private String RPM_LIST_SHELL="/usr/bin/zl1.sh";
	
	private static final String SHELL_SUCCESS_END_PREFIX="vcsok";
	private static final String SHELL_RECORD_BEGIN="vcs record begin";
	private static final String SHELL_RECORD_END="vcs record end";
	private static ReleaseInfoConfig releaseInfo=null;
	static {
		releaseInfo = new ReleaseInfoConfig();
	}
	
	 @RequestMapping("/refresh")
	 public ResponseData releaseList(ReleaseInfoConfig r) {
		 releaseInfo=r;
		 ResponseData success = ResponseData.success("");
		 success.put("info", releaseInfo);
		 return success;
	 }
	
    @RequestMapping("/internal/{version}/vcs/release/list")
    public ResponseData releaseList(String id) {
    	String installPackagePrefix = releaseInfo.getInstallPackagePrefix();
    	
    	List<InstallPackageInfo> list =getInstallPackageInfoList(installPackagePrefix,releaseInfo);
    	ResponseData success = ResponseData.success("");
    	success.put("list", list);
    	return success;
    }
    
    private List<InstallPackageInfo> getInstallPackageInfoList(String installPackagePrefix,
			ReleaseInfoConfig releaseInfo) {
    	if (StringUtils.isEmpty(installPackagePrefix)) return Collections.emptyList();
    	if (releaseInfo.getType() == ReleaseInfoType.rpm.getType()) {
    		List<String>  command = new ArrayList<String>();
    		command.add(SHELL_PARAM_PACKAGE);
    		command.add(installPackagePrefix);
    		command.add(SHELL_PARAM_DIR);
    		command.add(releaseInfo.getDir());
    		List<String> execList = execList(command);
    		return execList.stream().filter(c->c.startsWith(installPackagePrefix)).map(c->{
    			InstallPackageInfo i = new InstallPackageInfo();
    			String[] split = c.split(" ");
    			//TODO 此处try catch 原因：避免由于包命名原因而频繁改动代码。后期包命名规范后去除。
    			try {
    				i.setFullName(split[0]);
    				i.setSize(split[1]);
    				i.setLastModified(split[2] + " " + split[3]);
    				i.setPackageName(InstallPackageInfo.parsePackageName(split[0]));
    				i.setVersion(InstallPackageInfo.parseVersion(split[0]));
				} catch (Exception e) {
					e.getSuppressed();
				}
    			return i;
    		}).collect(Collectors.toList());
    	}
		return Collections.emptyList();
	}

	/**
     * @param info 发布信息
     * @param releaseType 1-version 发布, 2-完整包名发布
     * @return
     */
    @RequestMapping("/internal/{version}/vcs/release")
    public ResponseData release(ReleaseInfoConfig info,String version,String installPackage,int releaseType) {
    	if (releaseType == 1) {
    		return releaseByVersion(info,version);
    	} else if (releaseType == 2){
    		return releaseByInstallPackage(installPackage,ReleaseInfoConfig.parseVersion(installPackage),info.getPlatform());
    	} else {
    		//TODO
    		return ResponseData.fail("暂时不支持此方式发布");
    	}
    }
	
    private ResponseData releaseByInstallPackage(String installPackage,String version,String platform) {
		if (installPackage == null) {
			return ResponseData.fail("发布失败：错误的安装包");
		}
		List<String>  command = new ArrayList<String>();
		command.add(RPM_SHELL);
		command.add(SHELL_PARAM_PACKAGE);
		command.add(installPackage);
		command.add(SHELL_PARAM_PLATFORM);
		command.add(platform);
		command.add(SHELL_PARAM_VERSION);
		command.add(version);
		ResponseData exec = exec(command);
		return exec;
	}

	private ResponseData releaseByVersion(ReleaseInfoConfig info,String version) {
    	if (releaseInfo.getType() == ReleaseInfoType.rpm.getType()) {
    		String installPackage = releaseInfo.parseInstallPackage(version);
    		return releaseByInstallPackage(installPackage,version,info.getPlatform());
    	}else {
    		//TODO  exe || pkg
    		return ResponseData.fail("暂时不支持此类型发布");
    	}
	}
    
	private  List<String> execList(List<String> command) {
		ProcessBuilder pb = new ProcessBuilder(command);
		int runningStatus = 0;
		BufferedReader br = null;
		 List<String> list = new ArrayList<String>();
		try {
			logger.info("vcs exc shell command:" + StringUtils.join(command, " "));
			Process pro = pb.start();
			br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line;
			boolean startRecord=false;
			while ((line = br.readLine()) != null) {
				logger.info(line);
				if (line.startsWith(SHELL_RECORD_BEGIN)) {
					startRecord=true;
				} else if(line.startsWith(SHELL_RECORD_END)) {
					startRecord=false;
				}
				if(startRecord) {
					list.add(line);
				}
			}
			runningStatus = pro.waitFor();
			if (runningStatus != 0) {
				logger.info(runningStatus+"");
				return list;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	private  ResponseData exec(List<String> command) {
		ProcessBuilder pb = new ProcessBuilder(command);
		int runningStatus = 0;
		BufferedReader br = null;
		String result="操作失败";
		
		try {
			logger.info("vcs exc shell command:" + StringUtils.join(command, " "));
			Process pro = pb.start();
			br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			//StringBuffer strbr = new StringBuffer();
			String line;
			boolean execResult=false;
			while ((line = br.readLine()) != null) {
				logger.info(line);
				//strbr.append(line).append("\n");
				if (line.startsWith(SHELL_SUCCESS_END_PREFIX)) {
					execResult=true;
					break;
				}
			}
			runningStatus = pro.waitFor();
			if (runningStatus != 0) {
				System.out.println(runningStatus);
				return ResponseData.fail("Failed to release");
			}

			
			if (execResult) {
				return ResponseData.success("操作成功");
			}
		} catch (IOException e) {
			e.printStackTrace();
			result=e.getMessage();
		} catch (InterruptedException e) {
			e.printStackTrace();
			result=e.getMessage();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ResponseData.fail(result);
	}
}
