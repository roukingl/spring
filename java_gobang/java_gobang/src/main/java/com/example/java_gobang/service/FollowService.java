package com.example.java_gobang.service;


import com.example.java_gobang.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private FollowMapper followMapper;

    public int addUserFollowService(int userId, int followedId) {
        return followMapper.insertUserFollow(userId, followedId);
    }

    public int removeFollowService(int userId) {
        return followMapper.deleteUserFollow(userId);
    }
}
