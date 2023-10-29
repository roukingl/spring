package com.example.java_gobang.controller;

import com.example.java_gobang.common.AjaxResult;
import com.example.java_gobang.common.UserSessionUtils;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.entity.vo.UserVO;
import com.example.java_gobang.entity.vo.UserVOHall;
import com.example.java_gobang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        System.out.println(userCharacter);
        ArrayList<Integer> userIdList = userService.getUserIdListByCharacter(userCharacter);
        return AjaxResult.success(userIdList);
    }

    @PostMapping("/searchuserinformation")
    public AjaxResult searchUserInformation(@RequestParam("list") List<Integer> userIdList, HttpServletRequest request) {
        if (userIdList == null) {
            return AjaxResult.fail(1, "参数错误");
        }
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        System.out.println(Arrays.toString(userIdList.toArray()));
        // 返回用户的视图，增加一个属性表示是否为当前用户关注
        List<UserVO> userVOList = userService.getUserVOListById(userIdList, user);
        return AjaxResult.success(userVOList);
    }

    // 得到该用户关注的所有人和信息
    @PostMapping("/getfollowed")
    public AjaxResult getFollowedUser(HttpServletRequest request) {
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        List<User> followedList = userService.getFollowedUserService(user);
        return AjaxResult.success(followedList);
    }

    @PostMapping("/getfans")
    public AjaxResult getFansUser(HttpServletRequest request) {
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        List<UserVO> fansList = userService.getFansUserService(user);
        return AjaxResult.success(fansList);
    }


    @PostMapping("/getfollowedhall")
    public AjaxResult getFollowedUserHall(HttpServletRequest request) {
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        List<UserVOHall> followedHallList = userService.getFollowedUserHallService(user.getId());
        return AjaxResult.success(followedHallList);
    }

    @PostMapping("/getfanshall")
    public AjaxResult getFansUserHall(HttpServletRequest request) {
        User user = UserSessionUtils.getUser(request);
        if (user == null) {
            return AjaxResult.fail(2, "非法访问");
        }
        List<UserVOHall> fansHallList = userService.getFansUserHallService(user.getId());
        return AjaxResult.success(fansHallList);
    }


}
