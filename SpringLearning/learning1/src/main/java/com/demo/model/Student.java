package com.demo.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Student {
    private int id;
    private String name;
    private int age;
}
