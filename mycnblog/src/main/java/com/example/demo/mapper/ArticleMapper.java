package com.example.demo.mapper;

import com.example.demo.entity.Articleinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    int getArticleNumberByuid(@Param("uid") Integer uid);

    int getArticleNumber();

    List<Articleinfo> getMyList(@Param("uid") Integer uid);

    int deleteArticle(@Param("id") Integer id, @Param("uid") Integer uid);

    Articleinfo getArticleDetail(@Param("id") Integer id);

    int incrementRCount(@Param("id") Integer id);

    int insertArticle(Articleinfo articleinfo);

    int insertArticleTime(Articleinfo articleinfo);

    int updateTimeArticle(Articleinfo articleinfo);

    int updateArticle(Articleinfo articleinfo);

    /**
     * 分页查询
     *
     * @param psize   每页显示数目
     * @param offsize 所跳过的数目
     * @return 返回查询到的博客
     */
    List<Articleinfo> getListByPage(@Param("psize") Integer psize, @Param("offsize") Integer offsize);

    List<Articleinfo> getDraftList(@Param("uid") Integer uid);

    List<Articleinfo> getTimeArticle();

    List<Articleinfo> getTimeList(@Param("uid") Integer uid);

    int publishNowArticle(@Param("id") Integer id, @Param("uid") Integer uid);

    int publishCheckTime(Articleinfo articleinfo);
}
