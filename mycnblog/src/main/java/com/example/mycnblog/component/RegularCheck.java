package com.example.mycnblog.component;

import com.example.mycnblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class RegularCheck {

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/3 * * * * ? ")
    public void RegularCheckArticleService() {
        articleService.checkArticleTime();
    }
}
