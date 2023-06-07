package com.example.java_gobang.entity;

import lombok.Data;

@Data
public class DropsRequest {
    private String message;
    private int userId;
    private int row;
    private int col;
}
