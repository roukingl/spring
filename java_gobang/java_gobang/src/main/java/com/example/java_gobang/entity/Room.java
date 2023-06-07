package com.example.java_gobang.entity;

import com.example.java_gobang.JavaGobangApplication;
import com.example.java_gobang.component.OnlineUserState;
import com.example.java_gobang.component.RoomManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@Data
public class Room {
    private String roomId;

    // 一个房间里的两个用户
    private User user1;
    private User user2;

    // 谁是先手
    private int firstUserId;

    private ObjectMapper objectMapper = new ObjectMapper();

    private OnlineUserState onlineUserState;
    private RoomManager roomManager;

    private static final int MAX_ROW = 15;
    private static final int MAX_COL = 15;

    // 有三种状态，0 代表没人下，1表示用户1下的，2表示用户2下的
    private int[][] broad = new int[MAX_ROW][MAX_COL];

    @SneakyThrows
    public void putChess(String requestJSON) {
        DropsRequest dropsRequest = objectMapper.readValue(requestJSON, DropsRequest.class);
        if (dropsRequest.getMessage().equals("putChess")) {
            int chess = dropsRequest.getUserId() == user1.getId() ? 1 : 2;
            int row = dropsRequest.getRow();
            int col = dropsRequest.getCol();
            if (broad[row][col] != 0) {
                System.out.println("当前位置已经有子了 + row: " + row + " col: " + col);
                return;

            }
            broad[row][col] = chess;
            // 检查胜利信息，0 代表无人胜利，
            int winnerId = checkBroadSuccess(row, col, chess);
            // 返回棋盘打印棋子信息
            WebSocketSession session1 = onlineUserState.getSessionRoom(user1.getId());
            WebSocketSession session2 = onlineUserState.getSessionRoom(user2.getId());
            DropsResponse dropsResponse = new DropsResponse();
            dropsResponse.setMessage("putChess");
            dropsResponse.setRow(row);
            dropsResponse.setCol(col);
            dropsResponse.setWinUserId(winnerId);
            dropsResponse.setUserId(dropsRequest.getUserId());
            if (session1 == null) {
                 // 玩家一掉线，判断玩家二胜利
                 dropsResponse.setWinUserId(user1.getId());
            }
            if (session2 == null) {
                // 玩家二掉线，判断玩家一胜利
                dropsResponse.setWinUserId(user2.getId());
            }
            if (session1 != null) {
                // 如果玩家一在线就发给玩家一
                String response = objectMapper.writeValueAsString(dropsResponse);
                session1.sendMessage(new TextMessage(response));
            }
            if (session2 != null) {
                // 玩家二在下就发给玩家二
                String response = objectMapper.writeValueAsString(dropsResponse);
                session2.sendMessage(new TextMessage(response));
            }
        } else {
            // 请求不是putChess，发生错误，提醒客户端
            WebSocketSession session = onlineUserState.getSessionRoom(dropsRequest.getUserId());
            if (session == null) {
                // 掉线
                return;
            }
            DropsResponse dropsResponse = new DropsResponse();
            dropsResponse.setMessage("No Message");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(dropsResponse)));
        }

    }

    private int checkBroadSuccess(int row, int col, int chess) {
        return 0;
    }

    public Room() {
        // 创建roomId
        roomId = UUID.randomUUID().toString();
        onlineUserState = JavaGobangApplication.context.getBean(OnlineUserState.class);
        roomManager = JavaGobangApplication.context.getBean(RoomManager.class);
    }
}
