package com.example.demo.controller;

import com.example.demo.comon.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@RequestMapping("/user")
@RestController
public class UserController {

    private final Object lock = new Object();

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @PostMapping("/reg")
    public AjaxResult reg(Userinfo userinfo) {
        if (userinfo == null || !StringUtils.hasLength(userinfo.getUsername())
                || !StringUtils.hasLength(userinfo.getPassword())
                || userinfo.getPassword().length() < 6) {
            return AjaxResult.fail(0, "参数错误");
        }
        userinfo.setPassword(PasswordUtils.passwordEncrypt(userinfo.getPassword()));
        int row = userService.insertUserService(userinfo);
        return AjaxResult.success(row);
    }

    @RequestMapping("/login")
    public AjaxResult login(HttpServletRequest request, String username, String password) throws ParseException {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return AjaxResult.fail(0, "参数错误");
        }
        // 得到用户对象
        Userinfo userinfo = userService.getUserByNameService(username);
        if (userinfo == null || !StringUtils.hasLength(userinfo.getUsername())
                || !StringUtils.hasLength(userinfo.getPassword())) {
            return AjaxResult.fail(-1, "非法访问");
        }
        HashMap<Integer, Integer> freezeTimeMap = FreezeTimeEnum.getFreezeTimeList();
        int errorNumber = userinfo.getErrornum();
        int state = userService.getUserStateService(userinfo.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (errorNumber == 5) {
            // 1. 检差当前的 errornum 当等于5时，根据state来设置冻结时间
            int freeTimeDiffer = freezeTimeMap.get(state);
            LocalDateTime freetime = LocalDateTime.now().plusSeconds(freeTimeDiffer);
            userinfo.setFreezetime(freetime);
            userinfo.setErrornum(errorNumber + 1);
            int row = userService.updateStateFreezetimeErrornum(userinfo);
            Thread thread = new Thread(() -> {
                synchronized (lock) {
                    try {
                        lock.wait(FreezeTimeEnum.getFreezeTimeList().get(state) * 1000);
                        userService.updateUserErrornumState(userinfo.getId(), 0,
                                (userinfo.getState()) < 2 ? userinfo.getState() + 1: 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            return AjaxResult.fail(1, "账户已冻结，请 " +
                    (freeTimeDiffer >= 60 ? (freeTimeDiffer / 60) + "分钟后" : freeTimeDiffer + "秒后") + "重试");
        } else if (errorNumber > 5) {
            // 2. 当erronum 大于 5 时，返回剩余冻结时间
            String[] times = userinfo.getFreezetime().toString().split("T");
            Date date = simpleDateFormat.parse(times[0] + " " + times[1]);
            long time = (date.getTime() +  freezeTimeMap.get(state) - System.currentTimeMillis()) / 1000;
            String responseTime;
            if ( time > 60) {
                responseTime = (int) (time / 60) + "分钟  ";
                responseTime += time % 60 + "秒";
            } else {
                responseTime = (int) time + "秒";
            }
            return AjaxResult.fail(1, "账号已被冻结，请 "+ responseTime + " 后重试");
        }

        // 3. 没达到设定的 5 次就再次判断密码是否正确
        if (!PasswordUtils.passwordDecrypt(password, userinfo.getPassword())) {
            // 隐藏敏感信息   密码错误 再次增加 errornum
            userService.updateUserErrornum(userinfo.getId(), errorNumber + 1);
            return AjaxResult.fail(-1, "非法访问");
        }

        // 密码正确，清除 state 为0，errornum为0
        userinfo.setState(0);
        userinfo.setErrornum(0);
        userService.updateStateFreezetimeErrornum(userinfo);
        HttpSession session = request.getSession(true);
        userinfo.setPassword("");
        session.setAttribute(AppVariable.USER_SESSION_KEY, userinfo);
        return AjaxResult.success(userinfo);
    }

    @RequestMapping("/showinfo")
    public AjaxResult showUserInformation(HttpServletRequest request) {
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
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
        userinfoVO.setArticleNumber(articleService.getArticleNumberService(id));
        return AjaxResult.success(userinfoVO);
    }
}
