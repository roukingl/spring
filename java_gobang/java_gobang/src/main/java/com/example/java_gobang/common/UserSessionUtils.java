package com.example.java_gobang.common;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserSessionUtils {

    public static User getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(AppVariable.USER_SESSION_KEY) == null) {
            return null;
        }
        return (User) session.getAttribute(AppVariable.USER_SESSION_KEY);
    }
}
