package com.example.java_gobang.entity;

import lombok.Data;

@Data
public class DropsResponse {
    private String message;
    private int userId;
    private int row;
    private int col;
    private int winUserId;
}
