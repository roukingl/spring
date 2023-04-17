package com.example.demo.mapper;

import com.example.demo.entity.vo.ArticleinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper {

    /**
     * 通过id得到文章 和 作者
     * @param id 文章id
     * @return 返回的结果
     */
    ArticleinfoVO getArticleById(@Param("id") Integer id);
}
