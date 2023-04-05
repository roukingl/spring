package com.example.demo;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "student")
@Data
public class Student {
    private int id;
    private String name;
    private int age;

}
