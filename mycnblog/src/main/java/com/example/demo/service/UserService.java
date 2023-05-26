package com.example.demo.service;

import com.example.demo.entity.Userinfo;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int insertUserService(Userinfo userinfo) {
        return userMapper.insertUser(userinfo);
    }

    public Userinfo getUserByNameService(String username) {
        return userMapper.getUserByName(username);
    }

    public Userinfo getAuthorByIdService(Integer id) {
        return userMapper.getAuthorById(id);
    }
}
