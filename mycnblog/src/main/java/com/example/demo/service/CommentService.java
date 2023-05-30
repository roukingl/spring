package com.example.demo.service;

import com.example.demo.entity.Commentinfo;
import com.example.demo.entity.Userinfo;
import com.example.demo.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    /**
     * 往数据库中添加评论
     *
     * @param commentinfo 要添加的评论
     * @param user        当前登录的用户
     * @return 返回受影响的行数
     */
    public int addCommentService(Commentinfo commentinfo, Userinfo user) {
        commentinfo.setUid(user.getId());

        return commentMapper.insertComment(commentinfo);
    }

    /**
     * 查找所有该文章的所有评论
     *
     * @param articleId 改文章的id
     * @return 返回所有评论
     */
    public HashMap<String, Object> showCommentsService(Integer articleId) {
        List<Commentinfo> commentList = commentMapper.selectComments(articleId);
        List<String> usernameList = new ArrayList<>();
        for (Commentinfo comment : commentList) {
            int userId = comment.getUid();
            String username = commentMapper.selectUsernameByUid(userId);
            usernameList.add(username);
        }
        HashMap<String, Object> listMap = new HashMap<>();
        listMap.put("commentList", commentList);
        listMap.put("usernameList", usernameList);
        return listMap;
    }

}
