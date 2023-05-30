package com.example.demo.controller;

import com.example.demo.comon.AjaxResult;
import com.example.demo.comon.UserSessionUtils;
import com.example.demo.entity.Commentinfo;
import com.example.demo.entity.Userinfo;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加评论到这篇博客中，然后插入数据库
     * @param commentinfo 要插入的评论
     * @param request 请求
     * @return 返回统一格式
     */
    @RequestMapping("/add")
    public AjaxResult addComment(Commentinfo commentinfo, HttpServletRequest request) {
        if (commentinfo == null || !StringUtils.hasLength(commentinfo.getContent())) {
            return AjaxResult.fail(0, "参数错误");
        }
        if (commentinfo.getContent().length() > 50) {
            return AjaxResult.fail(2, "评论失败，请减少评论字数");
        }
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        int row = commentService.addCommentService(commentinfo, userinfo);
        return AjaxResult.success(row);
    }

    @RequestMapping("/showcomment")
    public AjaxResult showComments(@RequestParam("id") Integer articleId) {
        if (articleId == null || articleId <= 0) {
            return AjaxResult.fail(0, "参数错误");
        }
        HashMap<String, Object> listMap = commentService.showCommentsService(articleId);
        return AjaxResult.success(listMap);
    }
}
