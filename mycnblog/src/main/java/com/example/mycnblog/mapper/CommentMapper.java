package com.example.mycnblog.mapper;

import com.example.mycnblog.entity.Commentinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * 添加评论，在哪一篇博客，哪个人发布都
     * @param commentinfo 评论详情
     * @return 返回受影响的行数
     */
    int insertComment(Commentinfo commentinfo);

    /**
     * 根据文章id查询评论，进入文章详情时，就要展示在该博客下的评论
     * @param articleId 文章id
     * @return 评论的列表
     */
    List<Commentinfo> selectComments(@Param("articleId") Integer articleId);

    /**
     * 根据用户id查询用户名，每个评论要标识谁发的评论
     * @param userId 用户id
     * @return 用户名
     */
    String selectUsernameByUid(@Param("userId") Integer userId);
}
