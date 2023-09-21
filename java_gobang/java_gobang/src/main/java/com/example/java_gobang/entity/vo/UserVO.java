package com.example.java_gobang.entity.vo;

import com.example.java_gobang.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserVO extends User {
    private boolean whetherFollow;
}
