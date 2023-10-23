package com.example.java_gobang.component;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.MatchResponse;
import com.example.java_gobang.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class AttendHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OnlineUserState onlineUserState;

    // 连接后执行的方法
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 未登录的状态
            MatchResponse matchResponse = MatchResponse.fail(false, "您当前还未登录，不能进行后续匹配操作");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
            return;
        }

        // 退出界面后需要
        if (onlineUserState.getSessionHall(user.getId()) != null
            || onlineUserState.getSessionRoom(user.getId()) != null) {
            // 说明在大厅界面或游戏界面有多开
            MatchResponse matchResponse = MatchResponse.fail(true, "游戏禁止多开", "repeatConnect");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
            return;
        }
    }

    // 收到请求报文后执行的方法
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    // 程序发生异常后执行的方法
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    // 程序退出后执行的方法
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }
}
