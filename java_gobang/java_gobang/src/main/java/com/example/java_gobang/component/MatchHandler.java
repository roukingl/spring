package com.example.java_gobang.component;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.MatchRequest;
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
public class MatchHandler extends TextWebSocketHandler {

    @Autowired
    private OnlineUserState onlineUserState;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MatchQueue matchQueue;

    // 在连接成功上时执行
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从 websocketsession中 判断是否有没有改用户数据，无就是未登录，如果已经加入准备状态hash中
        // 那就是多开了，就要禁止，这两种以外的情况我们才把该用户放入准备hash中
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 未登录的状态
            MatchResponse matchResponse = MatchResponse.fail(false, "您当前还未登录，不能进行后续匹配操作");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
            return;
        }

        
        if (onlineUserState.getSessionHall(user.getId()) != null
            || onlineUserState.getSessionRoom(user.getId()) != null) {
            MatchResponse matchResponse = MatchResponse.fail(true, "游戏禁止多开", "repeatConnection");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
            return;
        }

        onlineUserState.enterSessionHall(user.getId(), session);
        System.out.println("玩家 + " + user.getUsername() + "进入准备状态");
    }

    // 在收到消息时执行
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 收到消息就要进行匹配
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 未登录的状态
            MatchResponse matchResponse = MatchResponse.fail(false, "您当前还未登录，不能进行后续匹配操作");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
            return;
        }
        // 当前这个数据载荷是一个 JSON 格式的字符串, 就需要把它转成 Java 对象. MatchRequest
        MatchRequest matchRequest = objectMapper.readValue(message.getPayload(), MatchRequest.class);
        MatchResponse matchResponse;
        if ("startMatch".equals(matchRequest.getMessage())) {
            // 进行匹配，需要加入匹配队列, 然后返回通知
            matchQueue.addUserToQueue(user);
            matchResponse = MatchResponse.success("startMatch");
        } else if ("stopMatch".equals(matchRequest.getMessage())) {
            // 取消匹配，移除匹配队列
            matchQueue.removeUserFromQueue(user);
            matchResponse = MatchResponse.success("stopMatch");
        } else {
            matchResponse = MatchResponse.fail(false, "非法请求");
        }
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse)));
    }


    // 在连接出现异常时执行
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 出现异常, 需要从准备hash中删除session
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 未登录的状态，直接返回
            return;
        }

        WebSocketSession tempsession = onlineUserState.getSessionHall(user.getId());
        if (tempsession == session) {
            // 当根据用户id从准备hash查到的session和前端传来的session相同时，才从准备hash中删除，表明
            // 这就是当前用户的操作
            onlineUserState.exitSessionHall(user.getId());
        }
        matchQueue.removeUserFromQueue(user);
    }
    // 在连接关闭时执行
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接断开, 需要从准备hash中删除session
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 未登录的状态，直接返回
            return;
        }

        WebSocketSession tempsession = onlineUserState.getSessionHall(user.getId());
        if (tempsession == session) {
            // 当根据用户id从准备hash查到的session和前端传来的session相同时，才从准备hash中删除，表明
            // 这就是当前用户的操作
            onlineUserState.exitSessionHall(user.getId());
        }
        matchQueue.removeUserFromQueue(user);
    }
}
