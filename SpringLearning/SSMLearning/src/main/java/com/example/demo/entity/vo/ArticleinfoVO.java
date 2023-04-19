package com.example.demo.entity.vo;

import com.example.demo.entity.Articleinfo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ArticleinfoVO extends Articleinfo implements Serializable {
    private final long serializableId = 1L;

    private String username;

    @Override
    public String toString() {
        return "ArticleinfoVO{" +
                "username='" + username + '\'' +
                "} " + super.toString();

    }
}
