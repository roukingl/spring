package com.example.demo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public HashMap<String, Object> exceptionHandler(Exception e) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "-1");
        map.put("msg", "异常 " + e.getMessage());
        map.put("data", null);
        return map;
    }

    @ExceptionHandler(NullPointerException.class)
    public HashMap<String, Object> nullExceptionHandler(NullPointerException e) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("state", "-1");
        map.put("msg", "空指针异常 " + e.getMessage());
        map.put("data", null);
        return map;
    }
}
