package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void getUserById() {
        Userinfo userinfo = userMapper.getUserById(1);
        System.out.println(userinfo);
    }

    @Test
    void getUserAll() {
        List<Userinfo> userinfoList = userMapper.getUserAll();
        System.out.println("userinfoList = " + userinfoList);
        Assertions.assertEquals(1, userinfoList.size());
    }

    @Test
    void addUser() {
        Userinfo userinfo = new Userinfo();
        userinfo.setUsername("李四");
        userinfo.setPassword("qwertyuiop");
        userinfo.setCreatetime(LocalDateTime.now());
        userinfo.setUpdatetime(LocalDateTime.now());
        int numberOfAffectedRows = userMapper.addUser(userinfo);
        System.out.println("numberOfAffectedRows = " + numberOfAffectedRows);
        Assertions.assertEquals(1, numberOfAffectedRows);
    }
}