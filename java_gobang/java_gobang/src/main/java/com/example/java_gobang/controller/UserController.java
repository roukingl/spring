package com.example.java_gobang.controller;

import com.example.java_gobang.common.AjaxResult;
import com.example.java_gobang.common.UserSessionUtils;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public AjaxResult userLogin(User user, HttpServletRequest request) {
        if (user == null || !StringUtils.hasLength(user.getUsername())
                || !StringUtils.hasLength(user.getPassword())) {
            return AjaxResult.fail(1, "参数异常");
        }
        boolean row = userService.getUserPasswordService(user.getUsername(), user.getPassword(), request);
        return AjaxResult.success(row);
    }

    @PostMapping("/register")
    public AjaxResult userRegister(User user) {
        if (user == null || !StringUtils.hasLength(user.getUsername())
                || !StringUtils.hasLength(user.getPassword())) {
            return AjaxResult.fail(1, "参数异常");
        }
        int row = userService.addUserService(user);
        return AjaxResult.success(row);
    }

    @PostMapping("/showuser")
    public AjaxResult intiShowUser(HttpServletRequest request) {
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        int userId = user.getId();
        User resultUser = userService.getUserByIdService(userId);
        return AjaxResult.success(resultUser);
    }

    @PostMapping("/searchuserid")
    public AjaxResult searchUserId(@RequestParam("userCharacter") String userCharacter, HttpServletRequest request) {
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        ArrayList<Integer> userIdList = userService.getUserIdListByCharacter(userCharacter);
        return AjaxResult.success(userIdList);
    }

    @PostMapping("/searchuserinformation")
    public AjaxResult searchUserInformation(@RequestParam("list") ArrayList<Integer> userIdList, HttpServletRequest request) {
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        ArrayList<User> userList = userService.getUserListById(userIdList);
        return AjaxResult.success(userList);
    }

}
