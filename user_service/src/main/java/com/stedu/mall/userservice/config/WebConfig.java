package com.stedu.mall.userservice.config;



import com.stedu.mall.common.interceptor.JwInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwInterceptor jwInterceptor;

    //配置对JWT进行校验的拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwInterceptor)
                .addPathPatterns("/user/**");
    }
}
