package com.example.java_gobang.service;

import com.example.java_gobang.entity.User;
import com.example.java_gobang.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int addUserService(User user) {
        // TODO 需要给注册密码进行加密
        return userMapper.insertUser(user);
    }

    public boolean getUserPassword(User user) {
        String username = user.getUsername();
        // TODO 数据库中的最终密码，要进行解密
        String password = user.getPassword();
        String finalPassword = userMapper.selectUserById(username);
        return password.equals(finalPassword);
    }
}
