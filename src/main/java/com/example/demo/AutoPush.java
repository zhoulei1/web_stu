package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rest")
public class AutoPush {
	
    @RequestMapping("/internal/{version}/vcs/syn")
    public void syn(String platform,String version) {
    	//return synPlatformJson;
    }
}
