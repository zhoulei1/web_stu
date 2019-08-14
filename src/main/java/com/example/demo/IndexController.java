package com.example.demo;

import com.example.demo.model.ResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import java.util.*;


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
}
