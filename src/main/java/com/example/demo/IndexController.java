package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
public class IndexController {
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
}
