package com.demo.learning;


import com.demo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StudentComponent {

    @Autowired
    private Student student;

    @Bean
    public Student getStudent() {
        student.setId(1);
        student.setName("张三");
        student.setAge(18);
        return student;
    }
}
