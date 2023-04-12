package com.example.demo.controller;

import com.example.demo.medol.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@Controller
@ResponseBody // 说明返回的是数据而不是页面
@Slf4j
@RequestMapping("/test")
public class TestController {

    @Autowired
    private Student stu;

    @PostConstruct
    public void postConstruct() {
        System.out.println(stu);
    }

    @RequestMapping("/hi") // url 路由映射
    public String sayHi(String name) {
        log.info("我是 info");
        if (!StringUtils.hasLength(name)) {
            name = "张三";
        }
        return "你好" + name;
    }
}
