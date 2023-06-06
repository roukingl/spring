package com.example.java_gobang.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Room {
    private String roomId;

    // 一个房间里的两个用户
    private User user1;
    private User user2;

    public Room() {
        // 创建roomId
        roomId = UUID.randomUUID().toString();
    }
}
