package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.ResponseData;


@Controller
public class IndexController {
    public final  Map<Integer,String> m=new HashMap<>();
    public final List<ResponseData> m1=new ArrayList<>();
    
    @RequestMapping(value = {"/jsp"})
    public String indexJsp(Model model) {
    	  model.addAttribute("name", "张三");
        return "index";
    }
    
    @RequestMapping(value = {"/tl"})
    public String indexThymeleaf(Model model) {
    	model.addAttribute("name", "张三");
        return "tl";
    }
    @RequestMapping("/data")
    @ResponseBody
    public JSONObject say(String name) {
    	JSONObject j = new JSONObject();
    	j.put("name", StringUtils.isEmpty(name)? "John Doe " :  name);
        return j;
    }

    @RequestMapping("/string")
    @ResponseBody
    public JSONObject string() {
        for (int i = 0; i < 10000; i++) {
            m.put(i, UUID.randomUUID().toString());
        }
        return new JSONObject();
    }
    @RequestMapping("/object")
    @ResponseBody
    public JSONObject object() {
        for (int i = 0; i < 10000; i++) {
            m1.add(new ResponseData(0,UUID.randomUUID().toString(),null));
        }
        return new JSONObject();
    }
    @RequestMapping("/l")
    @ResponseBody
    public JSONObject l() {
        JSONObject j = new JSONObject();
            j.put("mlength",m.size());
            j.put("m1length",m1.size());
        return j;
    }
	private final ExecutorService pool = Executors.newFixedThreadPool(1);
	private final ExecutorService pool2 = Executors.newFixedThreadPool(1);
	
    @RequestMapping("/pool1")
    @ResponseBody
    public JSONObject pool1() {
		pool.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println("doing something");
			}
		});
        return new JSONObject();
    }
    @RequestMapping("/pool2")
    @ResponseBody
    public JSONObject pool2() {
    	pool2.submit(new Runnable() {
    		@Override
    		public void run() {
    			int i=1/0;
    		}
    	});
    	return new JSONObject();
    }
	
}
