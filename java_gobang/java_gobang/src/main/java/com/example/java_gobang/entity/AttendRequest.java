package com.example.java_gobang.entity;

import lombok.Data;

@Data
public class AttendRequest {
    private String message;
    private int homeownerUserId;
    private int attendUserId;
}
