package com.stedu.mall.orderservice.service;

import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.OrderDetail;
import com.stedu.mall.common.service.OrderDetailService;
import com.stedu.mall.orderservice.mapper.OrderDetailMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;


    @Override
    public List<Goods> selectByGoodsId(Integer goodsId) {
        return orderDetailMapper.selectByGoodsId(goodsId);
    }

    @Override
    public void insert(List<OrderDetail> orderDetailList) {
        orderDetailMapper.insert(orderDetailList);
    }

    @Override
    public void delete(String orderId) {
        orderDetailMapper.delete(orderId);
    }

    @Override
    public void update(OrderDetail orderDetail) {
        orderDetailMapper.update(orderDetail);
    }
}
