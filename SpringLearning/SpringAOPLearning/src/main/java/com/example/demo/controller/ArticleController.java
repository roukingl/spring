package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arti")
public class ArticleController {

    @RequestMapping("/hi")
    public String sayHi() {
        return "hi world article";
    }

}
