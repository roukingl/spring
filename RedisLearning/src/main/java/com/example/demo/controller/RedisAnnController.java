package com.example.demo.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisAnnController {

    @RequestMapping("/get")
    @Cacheable(value = "Spring.cache", key = "#name+'-'+#number")
    public String get(String name, String number) {
        if (!StringUtils.hasLength(name) || !StringUtils.hasLength(number)) {
            return null;
        }
        System.out.println("执行了get方法");
        return "name: " + name + " | number: " + number;
    }

    @RequestMapping("/put")
    @CachePut(value = "Spring.cache", key = "#name+'-'+#number")
    public String put(String name, String number) {
        if (!StringUtils.hasLength(name) || !StringUtils.hasLength(number)) {
            return null;
        }
        System.out.println("执行了 put 方法");
        return "name + number : " + name + " + " +number;
    }

    @RequestMapping("/del")
    @CacheEvict(value = "Spring.cache", key = "#name+'-'+#number")
    public void del(String name, String number) {
        if (!StringUtils.hasLength(name) || !StringUtils.hasLength(number)) {
            return;
        }


        System.out.println("执行了 del 方法");
    }
}
