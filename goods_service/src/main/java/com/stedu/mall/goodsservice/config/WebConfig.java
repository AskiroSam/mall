package com.stedu.mall.goodsservice.config;

import com.stedu.mall.common.interceptor.JwInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${stedu.picDir}")
    private String picDir;
    @Autowired
    private JwInterceptor jwInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/category/pic/**", "/goods/pic/**")
                .addResourceLocations("file:" + picDir);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwInterceptor)
                .addPathPatterns("/goods/**", "/category/**")
                .excludePathPatterns("/category/allParent",
                        "/category/pic/**",
                        "/category/search",
                        "/goods/pic/**");
    }

}
