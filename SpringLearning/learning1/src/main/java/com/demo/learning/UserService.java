package com.demo.learning;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String sayHi() {
        return "Hi, service";
    }
}
