package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hi")
    public String satHi(String name, Integer age) {
        return "Hi " + name + " age " + age;
    }

    @GetMapping("show-user")
    public String showUser(User user) {
        return user.toString();
    }

    @GetMapping("show-time")
    public String showTime(@RequestParam(value = "t", required = false) String startTime,
                           @RequestParam("t2") String endTime) {
        // startTime / endTime
        return startTime + " " + endTime;
    }

    @PostMapping("show-json-user")
    public String showUserJSON(@RequestBody User user) {
        return user.toString();
    }
}
