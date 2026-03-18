package com.oestwdio.superpixelknights.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://.../RankResult/pvp/1.json 요청이 들어오면
        // 서버의 /data/ranks/pvp/1.json 파일을 찾아서 바로 보여줍니다.
        registry.addResourceHandler("/RankResult/**")
                .addResourceLocations("file:/data/ranks/");
    }
}