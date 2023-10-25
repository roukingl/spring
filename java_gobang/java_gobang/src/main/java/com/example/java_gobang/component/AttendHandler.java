package com.example.java_gobang.component;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.*;
import com.example.java_gobang.mapper.UserMapper;
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

    @Autowired
    private UserMapper userMapper;

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
        if ("attend".equals(attendRequest.getMessage())) {
            // 给邀请的那个人传递一个报文，你面包含发出邀请的那个人的姓名，用来辨识
            AttendResponse attendResponse = new AttendResponse();
            attendResponse.setOk(true);
            attendResponse.setMessage("attend");
            attendResponse.setHomeownerUser(user);
            attendResponse.setAttendUser(userMapper.selectUserById(attendRequest.getAttendUserId()));
            WebSocketSession attendSession = onlineUserState.getSessionDouble(attendRequest.getAttendUserId());
            attendSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(attendResponse)));
            return;
        } else if ("roomManager".equals(attendRequest.getMessage())) {
            // 是前端得到自己信息后加载房间状态
            boolean addSuccess = doubleRoomManager.enterDoubleRoom(user, userMapper.selectUserById(attendRequest.getHomeownerUserId()).getUsername());
        } else if ("enter".equals(attendRequest.getMessage())) {
            // 已经同意进入房间
            // 被邀请人退出原来的房间
            doubleRoomManager.exitDoubleRoom(user);
            // 被邀请人加入房间 TODO 加入房间前需要检测房间状态
            boolean addSuccess = doubleRoomManager.enterDoubleRoom(user, userMapper.selectUserById(attendRequest.getHomeownerUserId()).getUsername());
            User attendUser = userMapper.selectUserVOById(attendRequest.getAttendUserId());
            AttendResponse attendResponse = new AttendResponse();
            attendResponse.setOk(true);
            attendResponse.setMessage("enter");
            attendResponse.setAttendUser(attendUser);
            // 发送给房主被邀请人的信息来在前端展示
            WebSocketSession homeownerSession = onlineUserState.getSessionDouble(attendRequest.getHomeownerUserId());
            homeownerSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(attendResponse)));
        } else if ("playgame".equals(attendRequest.getMessage())) {
            // 检测到点击到开始游戏按钮 检查两个用户的房间状态 ，状态正常创建房间，房间加入房间管理器

        }
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
