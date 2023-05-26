package com.example.demo.comon;

import com.example.demo.entity.Userinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 和当前用户相关的操作
 */
public class UserSessionUtils {

    /**
     * 通过请求sessionid得到当前用户返回
     *
     * @param request
     * @return
     */
    public static Userinfo getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AppVariable.USER_SESSION_KEY) != null) {
            return (Userinfo) session.getAttribute(AppVariable.USER_SESSION_KEY);
        }
        return null;
    }
}
