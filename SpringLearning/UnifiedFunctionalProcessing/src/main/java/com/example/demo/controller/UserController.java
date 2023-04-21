package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getuser")
    public String getUser() {
        System.out.println("getuser");
        return "getUser 执行";
    }

    @RequestMapping("/login")
    public String login() {
        System.out.println("login~");
        return "login~";
    }

    @RequestMapping("/reg")
    public String reg() {
        System.out.println("reg~");
        return "reg~";
    }
}
