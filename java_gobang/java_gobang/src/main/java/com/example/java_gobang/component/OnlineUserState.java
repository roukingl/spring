package com.example.java_gobang.component;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@Component
public class OnlineUserState {
    private final HashMap<Integer, WebSocketSession> userGameHall = new HashMap<>();

    // 进入大厅就添加session
    public void enterSessionHall(Integer id, WebSocketSession session) {
        userGameHall.put(id, session);
    }

    // 退出大厅就删除session
    public void exitSessionHall(Integer id) {
        userGameHall.remove(id);
    }

    // 根据用户 id 得到session
    public WebSocketSession getSessionHall(Integer id) {
        return userGameHall.get(id);
    }

}
