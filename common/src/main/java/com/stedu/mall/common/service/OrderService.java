package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Order;

import java.util.List;

public interface OrderService {
    //添加
    boolean insert(Order Order);
    //根据id删除
    boolean delete(String id);
    //根据i修改
    boolean update(Order Order);
    //根据条件查询
    List<Order> selectByCondition(Order condition, Integer pageNum, Integer pageSize);
    //根据id查询
    Order selectById(String id);
}
