package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Userinfo implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String photo;
    private Date createtime;
    private Date updatetime;
    private Integer state;

}
