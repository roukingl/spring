package com.demo.learning;

import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    public String sayHello() {
        return "Hello, Controller";
    }
}
