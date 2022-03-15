package com.coding.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 정적 리소스 설정하기

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // application.properties에 설정한 uploadPath 프로퍼티 값을 읽어옴
    @Value("$(uploadPath)")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){

        // 웹 브라우저에 입력하는 url에 /images로 시작하는 경우
        // uplaodPath에 설정한 폴더를 기준으로 파일을 읽어오도록 설정
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
