package com.example.demo.config;


import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 自定义拦截器重写的函数
     *
     * @param request
     * @param response
     * @param handler
     * @return 如果为 true 表示验证成功，后面流程可以执行，为 false 是验证失败，后面流程无法执行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userinfo") != null) {
            return true;
        }
        // 可以重定向到登录页面, 也可以返回401/403
        response.sendRedirect("/login.html");
        return false;
    }
}
