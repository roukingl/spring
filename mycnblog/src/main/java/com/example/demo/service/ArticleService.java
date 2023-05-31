package com.example.demo.service;

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
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime ultimatelyTime = currentTime.plusSeconds(time);

        articleinfo.setUid(userinfo.getId());
        articleinfo.setState(2);
        articleinfo.setCreatetime(ultimatelyTime);
        articleinfo.setUpdatetime(ultimatelyTime);
        if (articleinfo.getId() > 0) {
            // 有效的文章id， 说明是从修改页面加载来的接口，那么久从原来的文章上修改
            return articleMapper.updateTimeArticle(articleinfo);
        }
        // 无效文章 说明是从增加页面加载的接口，插入一个记录
        return articleMapper.insertArticleTime(articleinfo);
    }

    // 发布
    public int updateArticleService(Articleinfo articleinfo, Userinfo userinfo) {
        articleinfo.setUid(userinfo.getId());
        articleinfo.setUpdatetime(LocalDateTime.now());
        if (articleinfo.getState() == 1) {
            articleinfo.setCreatetime(LocalDateTime.now());
        }
        articleinfo.setState(0);
        return articleMapper.updateArticle(articleinfo);
    }

    // 保存草稿
    public int saveDraftService(Articleinfo articleinfo, Userinfo userinfo) {
        articleinfo.setState(1);
        articleinfo.setUpdatetime(LocalDateTime.now());
        articleinfo.setUid(userinfo.getId());
        articleinfo.setRcount(1);
        return articleMapper.updateArticle(articleinfo);
    }

    public int publishTimeArticleService(Articleinfo articleinfo, Userinfo userinfo) {
        articleinfo.setState(0);
        articleinfo.setUid(userinfo.getId());
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

    public void checkArticleTime() {
        List<Articleinfo> list = articleMapper.getTimeArticle();
        if (list == null || list.size() == 0) {
            return;
        }
        for (Articleinfo articleinfo : list) {
            if (articleinfo.getCreatetime().isBefore(LocalDateTime.now())) {
                int row = publishCheckTime(articleinfo);
                // websocket 来主动通知用户定时发布文章成功
            }
        }
    }

    public int publishCheckTime(Articleinfo articleinfo) {
        return articleMapper.publishCheckTime(articleinfo);
    }

    public List<Articleinfo> getTimeArticleListService(Userinfo userinfo) {
        return articleMapper.getTimeList(userinfo.getId());
    }

    public int publishNowService(Integer id, Userinfo userinfo) {
        return articleMapper.publishNowArticle(id, userinfo.getId());
    }

    public List<Articleinfo> initGetArticleListService(Integer blogState, Userinfo userinfo) {
        return articleMapper.initGetArticleList(blogState, userinfo.getId());
    }
}
