package com.stedu.mall.orderservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stedu.mall")
@MapperScan(basePackages = {"com.stedu.mall.orderservice.mapper", "com.stedu.mall.goodsservice.mapper"})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
