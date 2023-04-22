package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 总的拦截器调配，先定义好拦截器和拦截器要做的工作，然后加到总的调配类中然后
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 拦截所有借口
                .excludePathPatterns("/blog/user/reg") // 要排除的借口
                .excludePathPatterns("/blog/user/login")
                .excludePathPatterns("/**/*.html")
                .excludePathPatterns("/blog/user/getrandom");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("blog", c -> true);
    }
}
