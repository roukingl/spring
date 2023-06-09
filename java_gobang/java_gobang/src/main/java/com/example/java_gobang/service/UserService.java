package com.example.java_gobang.service;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int addUserService(User user) {
        // TODO 需要给注册密码进行加密
        return userMapper.insertUser(user);
    }

    public boolean getUserPasswordService(String username, String password, HttpServletRequest request) {
        // TODO 数据库中的最终密码，要进行解密
        User user = userMapper.selectUserByName(username);
        if (user == null) {
            return false;
        }
        boolean isLogin = password.equals(user.getPassword());
        if (isLogin) {
            // 登录成功后添加 session
            HttpSession session = request.getSession(true);
            user.setPassword("");
            session.setAttribute(AppVariable.USER_SESSION_KEY, user);
            // TODO 每次登录成功后更新数据库密码盐值
        }
        return isLogin;
    }

    public User getUserByIdService(Integer userId) {
        return userMapper.selectUserById(userId);
    }

}
