package com.example.java_gobang.entity;

import lombok.Data;

@Data
public class ConnectResponse {
    private String message;
    private boolean ok;
    private String reason;
    private String roomId;
    private int thisUserId;
    private int thatUserId;
    // 先手用户id
    private int firstUserId;
}
