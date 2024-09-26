package com.stedu.mall.orderservice;

import com.stedu.mall.orderservice.mapper.OrderDetailMapper;
import com.stedu.mall.orderservice.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceApplicationTests {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Test
    void test01() {
        System.out.println(orderMapper.selectById("1762766797406343168"));
    }
    @Test
    void test02() {
        System.out.println(orderDetailMapper.selectById(1));
    }

}
