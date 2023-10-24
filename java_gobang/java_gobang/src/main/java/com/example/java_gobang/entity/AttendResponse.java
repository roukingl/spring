package com.example.java_gobang.entity;

import lombok.Data;

@Data
public class AttendResponse {

    private boolean ok;
    private String reason;
    private String message;
    private int attendUserId;
}
