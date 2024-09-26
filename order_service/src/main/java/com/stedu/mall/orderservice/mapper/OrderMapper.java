package com.stedu.mall.orderservice.mapper;

import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.Order;

import java.util.List;

public interface OrderMapper {
    //添加
    int insert(Order Order);
    //根据id删除
    int delete(Integer id);
    //根据i修改
    int update(Order Order);
    //根据条件查询
    List<Order> selectByCondition(Order condition);
    //根据id查询
    Order selectById(String id);
}
