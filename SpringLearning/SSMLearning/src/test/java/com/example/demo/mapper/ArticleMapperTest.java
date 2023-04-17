package com.example.demo.mapper;

import com.example.demo.entity.vo.ArticleinfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    @Transactional
    void getArticleById() {
        ArticleinfoVO articleinfoVO = articleMapper.getArticleById(1);
        System.out.println(articleinfoVO);
    }
}