package com.stedu.mall.goodsservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//scanBasePackages = 让springBoot扫描特定的包
@SpringBootApplication(scanBasePackages = "com.stedu.mall")
@MapperScan(basePackages = "com.stedu.mall.goodsservice.mapper")
@EnableDubbo
public class GoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsServiceApplication.class, args);
    }

}
