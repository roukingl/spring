package com.example.java_gobang.controller;

import com.example.java_gobang.common.AjaxResult;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.service.UserService;
import com.sun.xml.internal.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Object userLogin(User user) {
        if (user == null || !StringUtils.hasLength(user.getUsername())
                || !StringUtils.hasLength(user.getPassword())) {
            return AjaxResult.fail(-1, "参数异常");
        }
        boolean row = userService.getUserPassword(user);
        // TODO: 需要添加session

        return AjaxResult.success(row);
    }

    @PostMapping("/register")
    public Object userRegister(User user) {
        if (user == null || !StringUtils.hasLength(user.getUsername())
                || !StringUtils.hasLength(user.getPassword())) {
            return AjaxResult.fail(-1, "参数异常");
        }
        int row = userService.addUserService(user);
        return AjaxResult.success(row);
    }
}
