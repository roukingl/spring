package com.example.demo;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@Controller
@ResponseBody // 说明返回的是数据而不是页面
@PropertySource(value = "application.properties", encoding = "utf-8")
public class TestController {

    @Autowired
    private Student stu;

    @Value("${mytest}")
    private String myTest;

    @PostConstruct
    public void postConstruct() {
        System.out.println(stu);
    }

    @RequestMapping("/hi") // url 路由映射
    public String sayHi(String name) {

        if (!StringUtils.hasLength(name)) {
            name = "张三";
        }
        return "你好" + name;
    }

    @RequestMapping("/getconfig")
    public String getConfig() {
        return myTest;
    }
}
