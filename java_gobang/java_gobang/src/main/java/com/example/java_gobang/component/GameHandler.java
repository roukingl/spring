package com.example.java_gobang.component;

import com.example.java_gobang.common.AppVariable;
import com.example.java_gobang.entity.ConnectResponse;
import com.example.java_gobang.entity.DropsResponse;
import com.example.java_gobang.entity.Room;
import com.example.java_gobang.entity.User;
import com.example.java_gobang.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class GameHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private OnlineUserState onlineUserState;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        // 先创建一个连接响应对象，后面进行属性赋值然后返回给前端
        ConnectResponse connectResponse = new ConnectResponse();
        if (user == null) {
            // 用户没有登录，返回信息
            connectResponse.setOk(false);
            connectResponse.setReason("您当前没有登录，无法进行对战");
            String response = objectMapper.writeValueAsString(connectResponse);
            session.sendMessage(new TextMessage(response));
            return;
        }
        // 一个用户登录大厅页面，或登录房间页面，或既然登录大厅页面又登录房间页面就是多开，禁止
        if (onlineUserState.getSessionHall(user.getId()) != null
                || onlineUserState.getSessionRoom(user.getId()) != null) {
            // 游戏多开
            connectResponse.setOk(true);
            connectResponse.setReason("游戏禁止多开");
            connectResponse.setMessage("repeatConnection");
            String response = objectMapper.writeValueAsString(connectResponse);
            session.sendMessage(new TextMessage(response));
            return;
        }
        // 用户状态正常才把用户管理在房间哈希表中
        onlineUserState.enterSessionRoom(user.getId(), session);

        Room room = roomManager.getRoomByUserId(user.getId());
        if (room == null) {
            // 判断房间是否存在，没有就说明匹配的时候没有让房间和用户id关联起来，也就说明没有匹配成功
            connectResponse.setOk(false);
            connectResponse.setReason("当前没有匹配成功，不能进入游戏");
            String response = objectMapper.writeValueAsString(connectResponse);
            session.sendMessage(new TextMessage(response));
            return;
        }
        // 注意多线程问题
        synchronized (room) {
            if (room.getUser1() == null) {
                // 房间里用户1是空的，那就添加进去，并设置为先手
                room.setUser1(user);
                room.setFirstUserId(user.getId());
                System.out.println("玩家一准备就绪" + user.getUsername());
                return;
            }
            if (room.getUser2() == null) {
                room.setUser2(user);
                System.out.println("玩家二准备就绪" + user.getUsername());
                // 两个用户都添加进去后，就通知两个人就绪
                // 第一个通知用户一，thisUser是user1，thatUser是user2
                noticeGameReady(room, room.getUser1().getId(), room.getUser2().getId());
                // 第二个通知用户二，thisUser是user2，thatUser是user2
                noticeGameReady(room, room.getUser2().getId(), room.getUser1().getId());
                return;
            }
        }

        // 用户一和用户二都不为空就是房间满了，需要返回信息提醒用户三
        connectResponse.setOk(false);
        connectResponse.setReason("房间满了，当前不能对战");
        String response = objectMapper.writeValueAsString(connectResponse);
        session.sendMessage(new TextMessage(response));

    }

    @SneakyThrows
    private void noticeGameReady(Room room, int thisUserId, int thatUserId) {
        ConnectResponse connectResponse = new ConnectResponse();
        connectResponse.setOk(true);
        connectResponse.setMessage("gameReady");
        connectResponse.setRoomId(room.getRoomId());
        connectResponse.setThisUserId(thisUserId);
        connectResponse.setThatUserId(thatUserId);
        connectResponse.setFirstUserId(room.getFirstUserId());
        String response = objectMapper.writeValueAsString(connectResponse);
        WebSocketSession session = onlineUserState.getSessionRoom(thisUserId);
        session.sendMessage(new TextMessage(response));

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        ConnectResponse connectResponse = new ConnectResponse();
        if (user == null) {
            // 玩家不在房间页面了
            connectResponse.setOk(false);
            connectResponse.setReason("用户未登录");
            String response = objectMapper.writeValueAsString(connectResponse);
            session.sendMessage(new TextMessage(response));
            return;
        }
        // 通过该玩家id寻找房间
        Room room = roomManager.getRoomByUserId(user.getId());
        if (room == null) {
            // 房间为空，说明没有创建房间在房间管理器中，
            System.out.println("房间已空");
            return;
        }
        // 进行下棋
        room.putChess(message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // 程序异常，需要删除玩家在状态hash中的session
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            // 得不到就直接返回
            return;
        }
        WebSocketSession tmpSession = onlineUserState.getSessionRoom(user.getId());
        if (tmpSession == session) {
            // 只有从状态hash中得到的session和从前端传来的session相等时，才删除状态hash中的session
            onlineUserState.exitSessionRoom(user.getId());
        }
        // 通知对手获胜
        onticeThatWin(user);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 程序异常，需要删除玩家在状态hash中的session
        User user = (User) session.getAttributes().get(AppVariable.USER_SESSION_KEY);
        if (user == null) {
            return;
        }
        WebSocketSession tmpSession = onlineUserState.getSessionRoom(user.getId());
        if (tmpSession == session) {
            // 只有从状态hash中得到的session和从前端传来的session相等时，才删除状态hash中的session
            onlineUserState.exitSessionRoom(user.getId());
        }
        // 通知对手获胜
        onticeThatWin(user);
    }

    @SneakyThrows
    private void onticeThatWin(User user) {
        // 通过掉线用户得到房间
        Room room = roomManager.getRoomByUserId(user.getId());
        if (room == null) {
            // 房间已经销毁
            System.out.println("房间已经销毁");
            return;
        }
        // 通过房间来得到对手
        User thatUser = (user == room.getUser1() ? room.getUser2() : room.getUser1());
        WebSocketSession session = onlineUserState.getSessionRoom(thatUser.getId());
        if (session == null) {
            // 说明对手也掉线了，当前对局作废
            System.out.println("当前对局作废");
            return;
        }

        // 因为是落子期间掉线，所以构建一个落子响应
        DropsResponse dropsResponse = new DropsResponse();
        dropsResponse.setMessage("putChess");
        // 胜利的是掉线玩家的对手
        dropsResponse.setWinUserId(thatUser.getId());
        // 返回给掉线玩家对手页面展示
        dropsResponse.setUserId(thatUser.getId());
        String response = objectMapper.writeValueAsString(dropsResponse);
        session.sendMessage(new TextMessage(response));

        // 胜方败方数据修改
        int winId = thatUser.getId();
        int loseId = user.getId();
        userMapper.userWinUpdate(winId);
        userMapper.userLoseUpdate(loseId);
        // 从房间管理器中删除该房间
        roomManager.removeRoom(room.getRoomId(), room.getUser1().getId(), room.getUser2().getId());
    }
}
