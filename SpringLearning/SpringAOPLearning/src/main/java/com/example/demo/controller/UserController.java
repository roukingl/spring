package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @RequestMapping("/hi")
    public String sayHi(String name) {
        System.out.println("执行了 sayHi 方法");
        return "Hi " + name;
    }

    @RequestMapping("/hello")
    public String sayHello() {
        System.out.println("执行了 sayHello 方法");
        return "Hello World";
    }
}
