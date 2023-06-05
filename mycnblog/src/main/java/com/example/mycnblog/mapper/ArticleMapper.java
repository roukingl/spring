package com.example.mycnblog.mapper;

import com.example.mycnblog.entity.Articleinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    /**
     * 根据用户id来得到该作者的所有博客数量，用于显示个人信息
     *
     * @param uid   用户 id
     * @param state 返回博客的类型 0：发布的博客 1：博客草稿 2：定时发布的博客 3：删除的博客记录
     * @return 返回该用户博客数量
     */
    int getArticleNumberByuid(@Param("uid") Integer uid, @Param("state") Integer state);

    /**
     * 得到表中所有博客数量，用于分页操作
     * @return 返回博客总数
     */
    int getArticleNumber();

    /**
     * 得到该用户的所有发布博客
     * @param uid 该用户 id
     * @return 返回博客列表
     */
    List<Articleinfo> getMyList(@Param("uid") Integer uid);

    /**
     * 删除某篇博客当该作者操作时（其实时放入回收站）
     * @param id 博客id
     * @param uid 作者 id
     * @return 返回受影响的行数
     */
    int updateToDel(@Param("id") Integer id, @Param("uid") Integer uid);

    /**
     * 根据文章id来得到该博客详情
     * @param id 博客 id
     * @return 返回博客数据
     */
    Articleinfo getArticleDetail(@Param("id") Integer id);

    /**
     * 当有人点金该文章详情时就增加点击量
     * @param id 该博客 id
     * @return 受影响的行数
     */
    int incrementRCount(@Param("id") Integer id);

    /**
     * 发布博客（草稿博客和正常博客），需要添加标题，正文，作者，状态
     * @param articleinfo 要保存的博客信息
     * @return 返回受影响的行数
     */
    int insertArticle(Articleinfo articleinfo);

    /**
     * 当博客需要定时发布时，调用此接口，额外需要保存创建时间和修改时间
     * @param articleinfo 保存的博客信息
     * @return 返回受影响的行数
     */
    int insertArticleTime(Articleinfo articleinfo);

    /**
     * 进入修改页面才设置为定时发布博客
     * @param articleinfo 要修改博客的数据
     * @return 返回受影响的行数
     */
    int updateTimeArticle(Articleinfo articleinfo);

    /**
     * 从修改页面发布博客，额外修改修改时间
     * @param articleinfo 需要修改的博客数据
     * @return 返回受影响的行数
     */
    int updateArticle(Articleinfo articleinfo);

    /**
     * 分页查询
     * @param psize   每页显示数目
     * @param offsize 所跳过的数目
     * @return 返回查询到的博客
     */
    List<Articleinfo> getListByPage(@Param("psize") Integer psize, @Param("offsize") Integer offsize);

    /**
     * 在个人主页得到所有的发布（state == 1）博客
     * @param uid 博客作者 id
     * @return 返会发布博客列表
     */
    List<Articleinfo> getDraftList(@Param("uid") Integer uid);

    /**
     * 查询表中所有的定时发布的博客，来检查是否到时间发布
     * @return 返回所有的定时发布博客
     */
    List<Articleinfo> getTimeArticle();

    /**
     * 得到该用户的所有定时发布博客（state == 2）
     * @param uid 该用户id
     * @return 返回所有的定时发布博客
     */
    List<Articleinfo> getTimeList(@Param("uid") Integer uid);

    /**
     * 立即发布某一篇定时博客，还要修改发布时间和修改时间
     * @param id 该博客 id
     * @param uid 博客作者 id， 用于验证
     * @return 返回受影响的行数
     */
    int publishNowArticle(@Param("id") Integer id, @Param("uid") Integer uid);

    /**
     * 当定时发布的博客时间到时，主动发布博客，修改状态，不用修改时间
     * @param id 要发布的博客id
     */
    int publishCheckTime(@Param("id") Integer id);

    /**
     * 得到该用户以往删除的博客
     * @param uid 该用户id
     * @return 返回以往删除过的博客
     */
    List<Articleinfo> getDelList(@Param("uid") Integer uid);

    int deleteTrue(@Param("id") Integer id, @Param("uid") Integer uid);

}
