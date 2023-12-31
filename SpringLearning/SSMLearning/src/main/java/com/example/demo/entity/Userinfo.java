package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Userinfo implements Serializable {
    private final long serializableId = 1L;

    private int id;
    private String username;
    private String password;
    private String photo; // 头像
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
    private int state;
}
