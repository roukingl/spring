package com.example.demo.service;

import com.example.demo.entity.Commentinfo;
import com.example.demo.entity.Userinfo;
import com.example.demo.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 往数据库中添加评论
     * @param commentinfo 要添加的评论
     * @param user 当前登录的用户
     * @return 返回受影响的行数
     */
    public int addCommentService(Commentinfo commentinfo, Userinfo user) {
        commentinfo.setUid(user.getId());
        return commentMapper.insertComment(commentinfo);
    }
}
