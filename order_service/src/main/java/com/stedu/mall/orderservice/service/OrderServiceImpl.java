package com.stedu.mall.orderservice.service;

import com.stedu.mall.common.bean.Order;
import com.stedu.mall.common.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public boolean insert(Order Order) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public boolean update(Order Order) {
        return false;
    }

    @Override
    public List<Order> selectByCondition(Order condition, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public Order selectById(String id) {
        return null;
    }
}
