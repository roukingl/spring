package com.example.demo.config;

import com.example.demo.comon.AppVariable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AppVariable.USER_SESSION_KEY) != null) {
            // 能从 得到 session 返回true
            return true;
        }
        // 得不到 session, 重定向到登录页面
        response.sendRedirect("/login.html");
        return false;
    }
}
