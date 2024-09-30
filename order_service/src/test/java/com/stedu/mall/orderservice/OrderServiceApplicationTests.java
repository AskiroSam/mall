package com.stedu.mall.orderservice;

import com.stedu.mall.common.service.OrderService;
import com.stedu.mall.orderservice.mapper.OrderDetailMapper;
import com.stedu.mall.orderservice.mapper.OrderMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceApplicationTests {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderService orderService;

    @Test
    void test01() {
        System.out.println(orderDetailMapper.selectByOrderId("1762766797406343168"));
    }


}
