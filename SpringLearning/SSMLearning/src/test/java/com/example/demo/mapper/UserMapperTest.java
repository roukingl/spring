package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
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

    @Test
    void addUserGetId() {
        Userinfo userinfo = new Userinfo();
        userinfo.setUsername("王五");
        userinfo.setPassword("asbgasdfj");
        userinfo.setCreatetime(LocalDateTime.now());
        userinfo.setUpdatetime(LocalDateTime.now());
        int numberOfAffectedRows = userMapper.addUserGetId(userinfo);
        int id = userinfo.getId();
        System.out.println(id);
        Assertions.assertEquals(5, id);
    }

    @Test
    void updateUserName() {
        Userinfo userinfo = userMapper.getUserById(3);
        userinfo.setUsername("阿里斯顿");
        int numberOfAffectedRows = userMapper.updateUserName(userinfo);
        System.out.println("numberOfAffectedRows = " + numberOfAffectedRows);
        Assertions.assertEquals(1, numberOfAffectedRows);
    }

    @Test
    void deleteById() {
        Integer id = 5;
        int numberOfAffectedRows = userMapper.deleteById(id);
        System.out.println("numberOfAffectedRows = " + numberOfAffectedRows);
        Assertions.assertEquals(1, numberOfAffectedRows);
    }

    @Test
    void getUserAllByIdSortDesc() {
        List<Userinfo> list = userMapper.getUserAllByIdSortDesc("desc");
        System.out.println("list = " + list);
        Assertions.assertEquals(4, list.size());
    }
}