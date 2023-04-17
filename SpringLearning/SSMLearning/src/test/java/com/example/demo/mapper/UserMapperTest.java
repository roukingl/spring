package com.example.demo.mapper;

import com.example.demo.entity.Userinfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        Integer id = 7;
        int numberOfAffectedRows = userMapper.deleteById(id);
        System.out.println("numberOfAffectedRows = " + numberOfAffectedRows);
        Assertions.assertEquals(1, numberOfAffectedRows);
    }

    @Test
    void getUserAllByIdSort() {
        List<Userinfo> list = userMapper.getUserAllByIdSortDesc("desc");
        System.out.println("list = " + list);
        Assertions.assertEquals(4, list.size());
    }

    @Test
    void getUserByName() {
        String username = "m";
        List<Userinfo> list = userMapper.getUserByName(username);
        System.out.println("list = " + list);
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void addUser2() {
        Userinfo userinfo = new Userinfo();
        userinfo.setUsername("张三");
        userinfo.setPhoto(null);
        userinfo.setPassword("q12fwe");
        int numberOfAffectedRows = userMapper.addUser2(userinfo);
        Assertions.assertEquals(1, numberOfAffectedRows);
    }

    @Test
    void addUser3() {
        Userinfo userinfo = new Userinfo();
        userinfo.setUsername("bilibili");
        userinfo.setPassword("bilibili");
        userinfo.setPhoto("bilibili.jpg");
        int numberOfAffectedRows = userMapper.addUser3(userinfo);
        System.out.println(numberOfAffectedRows);
        Assertions.assertEquals(1, numberOfAffectedRows);
    }

    @Test
    void deleteUsersById() {
        List<Integer> ids = new ArrayList<>();
        ids.add(4);
        ids.add(9);
        userMapper.deleteUsersById(ids);
    }
}