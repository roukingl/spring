package com.example.demo.service;

import com.example.demo.comon.AjaxResult;
import com.example.demo.comon.UserSessionUtils;
import com.example.demo.entity.Articleinfo;
import com.example.demo.entity.Userinfo;
import com.example.demo.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    public ArticleMapper articleMapper;

    public int getArticleNumberService(Integer uid) {
        return articleMapper.getArticleNumberByuid(uid);
    }

    public List<Articleinfo> getMyListService(Integer uid) {
        return articleMapper.getMyList(uid);
    }

    public int deleteArticleService(Integer id, Integer uid) {
        return articleMapper.deleteArticle(id, uid);
    }

    public Articleinfo getArticleDetailService(Integer id) {
        return articleMapper.getArticleDetail(id);
    }

    public int incrementRCountService(Integer id) {
        return articleMapper.incrementRCount(id);
    }

    public int insertArticleService(Articleinfo articleinfo) {
        return articleMapper.insertArticle(articleinfo);
    }

    public int insertArticleService(Articleinfo articleinfo, Userinfo userinfo, String releaseTime) {
        // 得到前端传来的 2/30
        String[] releaseTimeList = releaseTime.split("/");
        // 计算成秒数
        long time = (Long.parseLong(releaseTimeList[0] )* 60 + Long.parseLong(releaseTimeList[1])) * 60;
        // 把秒数增加到当前时间，搞一个多线程来不断扫描是否达到时间，达到发布
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusSeconds(time);

        articleinfo.setUid(userinfo.getId());
        articleinfo.setState(2);
        articleinfo.setCreatetime(localDateTime);
        articleinfo.setUpdatetime(localDateTime);
        Thread thread = new Thread( () -> {
            while(articleinfo.getCreatetime().equals(LocalDateTime.now())) {
                updateArticleService(articleinfo, userinfo);
            }
        });
        return articleMapper.insertArticle(articleinfo);
    }

    public int updateArticleService(Articleinfo articleinfo, Userinfo userinfo) {
        articleinfo.setUid(userinfo.getId());
        articleinfo.setUpdatetime(LocalDateTime.now());
        if (articleinfo.getState() == 1) {
            articleinfo.setCreatetime(LocalDateTime.now());
        }
        articleinfo.setState(0);
        return articleMapper.updateArticle(articleinfo);
    }

    public int updateArticleService(Articleinfo articleinfo) {
        return articleMapper.updateArticle(articleinfo);
    }

    public List<Articleinfo> getListByPageService(Integer psize, Integer offsize) {
        return articleMapper.getListByPage(psize, offsize);
    }

    public int getArticleNumberService() {
        return articleMapper.getArticleNumber();
    }

    public List<Articleinfo> getDraftListService(Integer uid) {
        return articleMapper.getDraftList(uid);
    }

    public int deleteDraftService(Integer id, Integer uid) {
        return articleMapper.deleteDraft(id, uid);
    }
}
