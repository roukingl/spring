package com.example.demo.controller;

import com.example.demo.comon.AjaxResult;
import com.example.demo.comon.AppVariable;
import com.example.demo.comon.PasswordUtils;
import com.example.demo.comon.UserSessionUtils;
import com.example.demo.entity.Userinfo;
import com.example.demo.entity.vo.UserinfoVO;
import com.example.demo.service.ArticleService;
import com.example.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @PostMapping("/reg")
    public AjaxResult reg( Userinfo userinfo) {
        if (userinfo == null || !StringUtils.hasLength(userinfo.getUsername())
            || !StringUtils.hasLength(userinfo.getPassword())) {
            return AjaxResult.fail(-1, "参数错误");
        }
        userinfo.setPassword(PasswordUtils.passwordEncrypt(userinfo.getPassword()));
        int row = userService.insertUserService(userinfo);
        return AjaxResult.success(row);
    }

    @RequestMapping("/login")
    public AjaxResult login(HttpServletRequest request, String username, String password) {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return AjaxResult.fail(-1, "参数错误");
        }
        // 得到用户对象
        Userinfo userinfo = userService.getUserByNameService(username);
        if (userinfo != null && userinfo.getId() > 0) {
            // 有效的用户
            if (PasswordUtils.passwordDecrypt(password, userinfo.getPassword())) {
                // 隐藏敏感信息
                userinfo.setPassword("");
                // 密码正确
                HttpSession session = request.getSession(true);
                session.setAttribute(AppVariable.USER_SESSION_KEY, userinfo);
                return AjaxResult.success(userinfo);
            }
        }
        return AjaxResult.success(0, null);
    }

    @RequestMapping("/showinfo")
    public AjaxResult showUserInformation(HttpServletRequest request) {
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法请求");
        }
        Integer uid = userinfo.getId();
        UserinfoVO userinfoVO = new UserinfoVO();
        BeanUtils.copyProperties(userinfo, userinfoVO);
        userinfoVO.setArticleNumber(articleService.getArticleNumberService(uid));
        return AjaxResult.success(userinfoVO);
    }

    @RequestMapping("/logout")
    public AjaxResult UserLogout(HttpSession session) {
        session.removeAttribute(AppVariable.USER_SESSION_KEY);
        return  AjaxResult.success(1);
    }

    @RequestMapping("/showauthor")
    public AjaxResult showAuthorInformation(Integer id) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(0, "参数错误");
        }
        Userinfo userinfo = userService.getAuthorByIdService(id);
        if (userinfo == null || userinfo.getId() < 0) {
            return AjaxResult.success(-1, "非法响应");
        }
        userinfo.setPassword("");
        UserinfoVO userinfoVO = new UserinfoVO();
        BeanUtils.copyProperties(userinfo, userinfoVO);
        userinfoVO.setArticleNumber(articleService.getArticleNumberService(id));
        return AjaxResult.success(userinfoVO);
    }
}
