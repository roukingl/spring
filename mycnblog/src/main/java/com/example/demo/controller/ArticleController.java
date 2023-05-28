package com.example.demo.controller;

import com.example.demo.comon.AjaxResult;
import com.example.demo.comon.UserSessionUtils;
import com.example.demo.entity.Articleinfo;
import com.example.demo.entity.Userinfo;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@ResponseBody
@RequestMapping("/art")
@Controller
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping("/mylist")
    public AjaxResult getArticleList(HttpServletRequest request) {
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        List<Articleinfo> list = articleService.getMyListService(userinfo.getId());
        return AjaxResult.success(list);
    }

    @RequestMapping("/delete")
    public AjaxResult deleteArticle(HttpServletRequest request, @RequestParam("id") Integer id) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(0, "参数错误");
        }
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        int row = articleService.deleteArticleService(id, userinfo.getId());
        return AjaxResult.success(row);
    }

    @RequestMapping("/detail")
    public AjaxResult articleDetail(@RequestParam("id") Integer id) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(0, "参数错误");
        }
        Articleinfo articleinfo = articleService.getArticleDetailService(id);
        return AjaxResult.success(articleinfo);
    }

    @RequestMapping("/uprcount")
    public AjaxResult incrementRCount(@RequestParam("id") Integer id) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(0, "参数错误");
        }
        int row = articleService.incrementRCountService(id);
        return AjaxResult.success(row);
    }

    @RequestMapping("/add")
    public AjaxResult insertArt(Articleinfo articleinfo, HttpServletRequest request) {
        if (articleinfo == null || !StringUtils.hasLength(articleinfo.getTitle()) ||
                !StringUtils.hasLength(articleinfo.getContent())) {
            return AjaxResult.fail(0, "参数错误");
        }
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null || userinfo.getId() <= 0) {
            return AjaxResult.fail(-1, "非法访问");
        }
        articleinfo.setUid(userinfo.getId());
        int row = articleService.insertArticleService(articleinfo);
        return AjaxResult.success(row);
    }

    @RequestMapping("/draft")
    public AjaxResult draftSave(Articleinfo articleinfo, HttpServletRequest request) {
        if (articleinfo == null || (!StringUtils.hasLength(articleinfo.getTitle()) &&
                !StringUtils.hasLength(articleinfo.getContent()))) {
            return AjaxResult.fail(0, "参数错误");
        }
        // 设置草稿所属作者
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null || userinfo.getId() <= 0) {
            return AjaxResult.fail(-1, "非法访问");
        }
        articleinfo.setUid(userinfo.getId());
        articleinfo.setState(1);
        articleinfo.setUpdatetime(null);
        articleinfo.setCreatetime(null);
        int row = articleService.insertArticleService(articleinfo);
        return AjaxResult.success(row);
    }

    @RequestMapping("/update")
    public AjaxResult updateArt(Articleinfo articleinfo, HttpServletRequest request) {
        if (articleinfo == null || !StringUtils.hasLength(articleinfo.getTitle())
                || !StringUtils.hasLength(articleinfo.getContent()) || articleinfo.getId() <= 0) {
            return AjaxResult.fail(0, "参数错误");
        }
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        int row = articleService.updateArticleService(articleinfo, userinfo);
        return AjaxResult.success(row);
    }

    @RequestMapping("/listpage")
    public AjaxResult getListPage(@RequestParam("pindex") Integer pindex, @RequestParam("psize") Integer psize) {
        // 1. 参数校正
        if (pindex == null || pindex == 0) {
            pindex = 1;
        }
        if (psize == null || psize <= 1) {
            psize = 2;
        }
        // 2. 计算跳过的博客数量
        int offsize = (pindex - 1) * psize;
        // 3. 得到博客列表返回
        List<Articleinfo> list = articleService.getListByPageService(psize, offsize);
        int totalCount = articleService.getArticleNumberService();
        double pcountdb = totalCount / (psize * 1.0);
        int pcount = (int) Math.ceil(pcountdb);
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("pcount", pcount);
        return AjaxResult.success(map);
    }

    @RequestMapping("/draftlist")
    public AjaxResult getDraftList(HttpServletRequest request) {
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        List<Articleinfo> draftList = articleService.getDraftListService(userinfo.getId());
        return AjaxResult.success(draftList);
    }

    @RequestMapping("/save")
    public AjaxResult saveDraft(Articleinfo articleinfo, HttpServletRequest request) {
        if (articleinfo == null || !StringUtils.hasLength(articleinfo.getTitle())
                || !StringUtils.hasLength(articleinfo.getContent()) || articleinfo.getId() <= 0) {
            return AjaxResult.fail(0, "参数错误");
        }
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        int row = articleService.saveDraftService(articleinfo, userinfo);
        return AjaxResult.success(row);
    }

    // 增加页面和修改页面的定时博客借口分开
    @RequestMapping("/releasetime")
    public AjaxResult releaseTime(Articleinfo articleinfo, HttpServletRequest request, @RequestParam("releaseTime") String releaseTime) {
        if (articleinfo == null || releaseTime == null
                || !StringUtils.hasLength(articleinfo.getTitle())
                || !StringUtils.hasLength(articleinfo.getContent())) {
            return AjaxResult.fail(0, "参数错误");
        }
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        int row = articleService.insertArticleService(articleinfo, userinfo, releaseTime);
        return AjaxResult.success(row);
    }

    // timelist 展示该用户所有的定时发布的文章
    @RequestMapping("/timelist")
    public AjaxResult getTimeList(HttpServletRequest request) {
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        List<Articleinfo> list = articleService.getTimeArticleListService(userinfo);
        return AjaxResult.success(list);
    }

    // publishnow 立即发布当前的定时文章
    @RequestMapping("/publishnow")
    public AjaxResult publishNowArt(@RequestParam("id") Integer id, HttpServletRequest request) {
        Userinfo userinfo = UserSessionUtils.getUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法访问");
        }
        int row = articleService.publishNowService(id, userinfo);
        return AjaxResult.success(row);
    }

}
