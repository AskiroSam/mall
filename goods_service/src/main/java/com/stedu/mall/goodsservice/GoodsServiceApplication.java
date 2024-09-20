package com.stedu.mall.goodsservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//scanBasePackages = 让springBoot扫描特定的包
@SpringBootApplication(scanBasePackages = "com.stedu.mall")
@MapperScan(basePackages = "com.stedu.mall.goodsservice.mapper")
public class GoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsServiceApplication.class, args);
    }

}
