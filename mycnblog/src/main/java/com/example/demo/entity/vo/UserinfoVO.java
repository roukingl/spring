package com.example.demo.entity.vo;

import com.example.demo.entity.Userinfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserinfoVO extends Userinfo implements Serializable {
    private int articleNumber;
}
