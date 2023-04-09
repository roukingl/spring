package com.example.demo.model;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class User {

    private int id;
    private String name;
    private int age;
}
