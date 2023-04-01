package com.demo.learning;

import com.demo.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class StudentComponent {

    @Bean
    public Student getStudent() {
        Student stu = new Student();
        stu.setId(1);
        stu.setName("张三");
        stu.setAge(18);
        return stu;
    }
}
