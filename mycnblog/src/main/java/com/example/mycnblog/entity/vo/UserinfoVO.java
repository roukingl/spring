package com.example.mycnblog.entity.vo;

import com.example.mycnblog.entity.Userinfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserinfoVO extends Userinfo implements Serializable {
    private int articleNumber;
}
