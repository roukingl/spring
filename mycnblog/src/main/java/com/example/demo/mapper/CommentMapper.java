package com.example.demo.mapper;

import com.example.demo.entity.Commentinfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    /**
     * 插入评论
     * @param commentinfo 要插入的评论数据
     * @return 返回受影响的行数
     */
    int insertComment(Commentinfo commentinfo);
}
