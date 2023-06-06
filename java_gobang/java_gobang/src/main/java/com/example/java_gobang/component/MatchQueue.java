package com.example.java_gobang.component;

import com.example.java_gobang.entity.MatchResponse;
import com.example.java_gobang.entity.Room;
import com.example.java_gobang.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class MatchQueue {

    @Autowired
    private OnlineUserState onlineUserState;

    @Autowired
    private RoomManager roomManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 天梯积分小于2000
    private final Queue<User> normalQueue = new LinkedList<>();
    // 天梯积分大于等于2000，小于3000
    private final Queue<User> highQueue = new LinkedList<>();
    // 天梯积分大于等于3000
    private final Queue<User> veryHighQueue = new LinkedList<>();

    public void addUserToQueue(User user) {
        if (user.getScore() < 2000) {
            synchronized (normalQueue) {
                normalQueue.offer(user);
                normalQueue.notify();
            }
        } else if (user.getScore() < 3000) {
            synchronized (highQueue) {
                highQueue.offer(user);
                highQueue.notify();
            }
        } else {
            synchronized (veryHighQueue) {
                veryHighQueue.offer(user);
                veryHighQueue.notify();
            }
        }
    }

    public void removeUserFromQueue(User user) {
        if (user.getScore() < 2000) {
            synchronized (normalQueue) {
                normalQueue.remove(user);
            }
        } else if (user.getScore() < 3000) {
            synchronized (highQueue) {
                highQueue.remove(user);
            }
        } else {
            synchronized (veryHighQueue) {
                veryHighQueue.remove(user);
            }
        }
    }

    public MatchQueue() {
        // 创建三个线程，分别循环查找三个队列进行匹配
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                // 扫描highQueue
                while (true) {
                    handlerMatch(highQueue);
                }
            }
        };
        thread1.start();
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                // 扫描veryHighQueue
                while (true) {
                    handlerMatch(veryHighQueue);
                }
            }
        };
        thread2.start();
        Thread thread3 = new Thread() {
            @Override
            public void run() {
                // 扫描normalQueue
                while (true) {
                    handlerMatch(normalQueue);
                }
            }
        };
        thread3.start();
    }

    private void handlerMatch(Queue<User> queue) {
        synchronized (queue) {
            try {
                while (queue.size() < 2) {
                    queue.wait();
                }
                User player1 = queue.poll();
                User player2 = queue.poll();
                System.out.println("两个玩家 " + player1.getUsername() + " " + player2.getUsername());

                // 从状态hash中取出websocketsession 来通知客户端比配到了对局，来跳转页面进行对局
                WebSocketSession session1 = onlineUserState.getSessionHall(player1.getId());
                WebSocketSession session2 = onlineUserState.getSessionHall(player2.getId());
                if (session1 == null) {
                    // 如果取出来的session1为空，说明用户1不在状态hash中，就需要把用户2放回去
                    queue.offer(player2);
                    return;
                }
                if (session2 == null) {
                    queue.offer(player1);
                    return;
                }
                if (session1 == session2) {
                    // 说明两个人是同一个人，不能进行对局，放回去
                    queue.offer(player1);
                    return;
                }

                // 匹配完成后就需要放入一个房间进行对战
                Room room = new Room();
                roomManager.addRoom(room, player1.getId(), player2.getId());

                // 给玩家反馈消息匹配成功，返回matchSuccess
                MatchResponse matchResponse1 = MatchResponse.success("matchSuccess");
                MatchResponse matchResponse2 = MatchResponse.success("matchSuccess");
                session1.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse1)));
                session2.sendMessage(new TextMessage(objectMapper.writeValueAsString(matchResponse2)));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

