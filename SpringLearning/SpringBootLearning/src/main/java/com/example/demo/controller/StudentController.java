package com.example.demo.controller;

import com.example.demo.medol.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StudentController {

    @Bean
    public Student getStudent() {
        Student stu = new Student();
        stu.setId(1);
        stu.setName("王五");
        stu.setAge(16);
        return stu;
    }
}
