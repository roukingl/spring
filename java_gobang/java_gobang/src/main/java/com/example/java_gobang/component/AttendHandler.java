package com.example.java_gobang.component;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.AttendRequest;
import com.example.java_gobang.entity.AttendResponse;
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

    @Autowired
    private DoubleRoomManager doubleRoomManager;

    // 连接后执行的方法
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 未登录的状态
            AttendResponse attendResponse = new AttendResponse();
            attendResponse.setReason("你当前没有登录，不能操作");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(attendResponse)));
            return;
        }

        if (onlineUserState.getSessionHall(user.getId()) != null
            || onlineUserState.getSessionRoom(user.getId()) != null
            || onlineUserState.getSessionDouble(user.getId()) != null) {
            // 说明在大厅界面或游戏界面有多开
            AttendResponse attendResponse = new AttendResponse();
            attendResponse.setOk(true);
            attendResponse.setReason("游戏禁止多开");
            attendResponse.setMessage("repeatConnect");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(attendResponse)));
            return;
        }

        // 需要一个管理创建房间的状态 Map
        onlineUserState.enterSessionDouble(user.getId(), session);
        // 每个用户进入房间得到所有在房间里的用户信息
        doubleRoomManager.enterDoubleRoom(user, String.valueOf(session.getTextMessageSizeLimit()));

        System.out.println(user.getUsername() + "进入自建房间") ;
    }

    // 收到请求报文后执行的方法
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 根据响应判断是邀请报文还是开始游戏报文，
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            AttendResponse attendResponse = new AttendResponse();
            attendResponse.setOk(false);
            attendResponse.setReason("当前未登录，请登录后操作");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(attendResponse)));
            return;
        }
        AttendRequest attendRequest = objectMapper.readValue(message.getPayload(), AttendRequest.class);
        AttendResponse attendResponse = new AttendResponse();
        if ("attend".equals(attendRequest.getMessage())) {
            // 得到邀请的
            doubleRoomManager.enterDoubleRoom(user, attendRequest.getHomeownerUsername());
            attendResponse.setOk(true);
            attendResponse.setMessage("attend");
            WebSocketSession attendSession = onlineUserState.getSessionDouble(attendRequest.getAttendUserId());
            attendSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(attendResponse)));
            return;
        } else if ("") {
            // 按钮是已邀请的
        } else if {
            // 按钮是开始游戏的
        }
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(attendResponse)));
    }

    // 程序发生异常后执行的方法
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        //
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 未登录，直接返回
            return;
        }
        // 从自建房间状态 Map 中查找 session，从中查找到的 session 和从前端传递过来的一样，表明是一个人，直接删除状态
        WebSocketSession tmpSession = onlineUserState.getSessionDouble(user.getId());
        if (tmpSession == session) {
            onlineUserState.exitSessionDouble(user.getId());
        }
        doubleRoomManager.exitDoubleRoom(user);
    }

    // 程序退出后执行的方法
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 关闭连接了需要把房间管理中用户删除,分房主和邀请人
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 未登录，直接返回
            return;
        }
        // 从自建房间状态 Map 中查找 session，从中查找到的 session 和从前端传递过来的一样，表明是一个人，直接删除状态
        WebSocketSession tmpSession = onlineUserState.getSessionDouble(user.getId());
        if (tmpSession == session) {
            onlineUserState.exitSessionDouble(user.getId());
        }
        doubleRoomManager.exitDoubleRoom(user);
    }
}
