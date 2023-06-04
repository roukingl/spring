package com.example.demo.common;

import com.example.demo.entity.Userinfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 和当前用户相关的操作
 */
public class UserSessionUtils {

    /**
     * 通过请求 sessionId 得到当前用户返回
     * @param request 从客户端得到的请求
     * @return 返回从该请求session中得到的用户
     */
    public static Userinfo getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AppVariable.USER_SESSION_KEY) != null) {
            return (Userinfo) session.getAttribute(AppVariable.USER_SESSION_KEY);
        }
        return null;
    }
}
