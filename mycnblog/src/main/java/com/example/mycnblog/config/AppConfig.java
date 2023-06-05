package com.example.mycnblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new com.example.mycnblog.config.LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/editor.md/**")
                .excludePathPatterns("/img/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/reg.html")
                .excludePathPatterns("/login.html")
                .excludePathPatterns("/blog_list.html")
                .excludePathPatterns("/blog_content.html")
                .excludePathPatterns("/draft_list.html")
                .excludePathPatterns("/art/detail")
                .excludePathPatterns("/art/uprcount")
                .excludePathPatterns("/art/listpage")
                .excludePathPatterns("/user/showauthor")
                .excludePathPatterns("/user/reg")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/updatepass")
                .excludePathPatterns("/comment/showcomment")
                .excludePathPatterns("/comment/add");
    }
}
