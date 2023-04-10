package com.example.demo.controller;

import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@ResponseBody
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hi")
    public String satHi(String name, Integer age) {
        return "Hi " + name + " age " + age;
    }

    @GetMapping("/show-user")
    public String showUser(User user) {
        return user.toString();
    }

    @GetMapping("/show-time")
    public String showTime(@RequestParam(value = "t", required = false) String startTime,
                           @RequestParam("t2") String endTime) {
        // startTime / endTime
        return startTime + " " + endTime;
    }

    @PostMapping("/show-json-user")
    public String showUserJSON(@RequestBody User user) {
        return user.toString();
    }

    // 从基础 url 中获取参数
    @PostMapping("/login/{name}/{password}")
    public String pathGetParameter1(@PathVariable("name") String userName,
                                   @PathVariable("password") String userPassword) {
        return "userName " + userName + " | userPassword" + userPassword;
    }

    @RequestMapping("show/{name}/and/{password}")
    public String pathGetParameter(@PathVariable("password") String pwd,
                                   @PathVariable("name") String userName) {
        return "userName " + userName + " | pwd " + pwd;
    }

    @RequestMapping("/upfile")
    public String upFile(@RequestPart("myfile")MultipartFile file) throws IOException {
        String path = "D:\\Files\\test.png";
        file.transferTo(new File(path));
        return path;
    }

    @RequestMapping("/myupfile")
    public String myUpFile(@RequestPart("myfile") MultipartFile file) throws IOException {
        // 基础目录
        String path = "D:\\Files\\";
        // 加上随机文件名
        path += UUID.randomUUID().toString();
        // 加上文件名后缀
        String originalFileName = file.getOriginalFilename();
        path += originalFileName.substring(originalFileName.lastIndexOf("."));
        file.transferTo(new File(path));
        return path;
    }

    @RequestMapping("getparam")
    public String getParam(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("name");
        return userName;
    }
}
