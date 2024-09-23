package com.stedu.mall.goodsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${stedu.picDir}")
    private String picDir;

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/category/pic/**", "/goods/pic/**")
                .addResourceLocations("file:" + picDir);
    }
}
