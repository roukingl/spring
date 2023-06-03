package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getrandom")
    public int getRandom() {
        return new Random().nextInt(10);
    }

    @RequestMapping("/getuser")
    public String getUser() {
        System.out.println("getuser");
        return "getUser 执行";
    }

    @RequestMapping("/login")
    public String login() {
        System.out.println("login~");
        int i = 10 / 0;
        return "login~";
    }

    @RequestMapping("/reg")
    public String reg() {
        System.out.println("reg~");
        return "reg~";
    }
}
