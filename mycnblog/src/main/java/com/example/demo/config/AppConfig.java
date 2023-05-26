package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
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
                .excludePathPatterns("/art/draft")
                .excludePathPatterns("/art/save")
                .excludePathPatterns("/art/deldraft")
                .excludePathPatterns("/art/draftlist")
                .excludePathPatterns("/art/listpage")
                .excludePathPatterns("/art/releasetime")
                .excludePathPatterns("/art/timelist")
                .excludePathPatterns("/art/publishnow")
                .excludePathPatterns("/user/showauthor")
                .excludePathPatterns("/user/reg")
                .excludePathPatterns("/user/login");
    }
}
