package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/html")
public class HtmlController {

    @RequestMapping("/index")
    public String getHtml() {
        return "/index.html";
    }


}
