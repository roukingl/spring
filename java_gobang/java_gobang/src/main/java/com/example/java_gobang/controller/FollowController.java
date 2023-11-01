package com.example.java_gobang.controller;

import com.example.java_gobang.common.AjaxResult;
import com.example.java_gobang.common.UserSessionUtils;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/add")
    public AjaxResult addUserFollow(@RequestParam("followedId") Integer followedId, HttpServletRequest request) {
        if (followedId == null || followedId <= 0) {
            return AjaxResult.fail(1, "参数异常");
        }
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        int row = followService.addUserFollowService(user.getId(), followedId);
        return AjaxResult.success(row);
    }

    @PostMapping("/delete")
    public AjaxResult removeUserFollow(@RequestParam("followedId") Integer followedId, HttpServletRequest request) {
        if (followedId == null || followedId <= 0) {
            return AjaxResult.fail(1, "参数异常");
        }
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        int row = followService.removeFollowService(user.getId(), followedId);
        return AjaxResult.success(row);
    }

}
