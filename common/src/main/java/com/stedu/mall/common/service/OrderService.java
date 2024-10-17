package com.stedu.mall.common.service;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.bean.Order;
import com.stedu.mall.common.bean.OrderDetail;
import com.stedu.mall.common.bean.OrderVo;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;

public interface OrderService {
    //添加
    void insert(OrderVo orderVo) throws SteduException;
    //根据id删除
    void delete(String id) throws SteduException;
    //根据i修改
    void update(Order Order);
    //根据条件查询
    PageInfo<Order> selectByCondition(Order condition, Integer pageNum, Integer pageSize);
    //根据id查询
    Order selectById(String id, Integer userId) throws SteduException;

    //查询所有
    List<Order> selectAll();
    //支付
    void pay(OrderVo orderVo) throws SteduException;
    //检查订单超时时间
    int checkOrderTime(String id) throws SteduException;
}
