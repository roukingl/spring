package com.example.demo.mapper;

import com.example.demo.entity.Commentinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {


    int insertComment(Commentinfo commentinfo);

    List<Commentinfo> selectComments(@Param("articleId") Integer articleId);

    String selectUsernameByUid(@Param("userId") Integer userId);
}
