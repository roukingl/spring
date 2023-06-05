package com.example.mycnblog.controller;

import com.example.mycnblog.common.*;
import com.example.mycnblog.entity.Userinfo;
import com.example.mycnblog.entity.vo.UserinfoVO;
import com.example.mycnblog.service.ArticleService;
import com.example.mycnblog.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @PostMapping("/reg")
    public AjaxResult register(Userinfo userinfo) {
        if (userinfo == null || !StringUtils.hasLength(userinfo.getUsername())
                || !StringUtils.hasLength(userinfo.getPassword())
                || userinfo.getPassword().length() < 6) {
            return AjaxResult.fail(0, "参数错误");
        }
        int row = userService.insertUserService(userinfo);
        return AjaxResult.success(row);
    }

    @RequestMapping("/login")
    public AjaxResult userLogin(HttpServletRequest request, String username, String password) throws ParseException {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return AjaxResult.fail(0, "参数错误");
        }
        // 得到用户对象
        Userinfo userinfo = userService.getUserByNameService(username);
        if (userinfo == null || !StringUtils.hasLength(userinfo.getUsername())
                || !StringUtils.hasLength(userinfo.getPassword())) {
            return AjaxResult.fail(-1, "非法访问");
        }
        return userService.userLoginService(password, userinfo, request);
    }

    @RequestMapping("/showinfo")
    public AjaxResult showUserInformation(@RequestParam("state") Integer state, HttpServletRequest request) {
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        Integer uid = userinfo.getId();
        UserinfoVO userinfoVO = new UserinfoVO();
        BeanUtils.copyProperties(userinfo, userinfoVO);
        userinfoVO.setArticleNumber(articleService.getArticleNumberService(uid, state));
        return AjaxResult.success(userinfoVO);
    }

    @RequestMapping("/logout")
    public AjaxResult userLogout(HttpSession session) {
        session.removeAttribute(AppVariable.USER_SESSION_KEY);
        if (session.getAttribute(AppVariable.USER_SESSION_KEY) != null) {
            // 删除失败，返回数据 0
            return AjaxResult.success(0);
        }
        return AjaxResult.success(1);
    }

    @RequestMapping("/showauthor")
    public AjaxResult showAuthorInformation(Integer id) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(0, "参数错误");
        }
        Userinfo userinfo = userService.getAuthorByIdService(id);
        if (userinfo == null || userinfo.getId() < 0) {
            return AjaxResult.success(-1, "非法访问");
        }
        userinfo.setPassword("");
        UserinfoVO userinfoVO = new UserinfoVO();
        BeanUtils.copyProperties(userinfo, userinfoVO);
        userinfoVO.setArticleNumber(articleService.getArticleNumberService(id, 0));
        return AjaxResult.success(userinfoVO);
    }

    @RequestMapping("/updatepass")
    public AjaxResult updatePassword(String beforePassword, String afterPassword, HttpServletRequest request) {
        if (!StringUtils.hasLength(beforePassword)
            || !StringUtils.hasLength(afterPassword) || afterPassword.length() < 6) {
            return AjaxResult.fail(0, "参数错误");
        }
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        String databasePassword = userService.getUserPassword(userinfo.getId());
        if (!PasswordUtils.passwordDecrypt(beforePassword, databasePassword)) {
            return AjaxResult.fail(0, "密码输入错误，请重新输入");
        }
        userinfo.setPassword(PasswordUtils.passwordEncrypt(afterPassword));
        int row = userService.updateUserPasswordService(userinfo);
        return AjaxResult.success(row);
    }
}
