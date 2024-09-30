package com.stedu.mall.orderservice.service;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.*;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.GoodsService;
import com.stedu.mall.common.service.OrderService;
import com.stedu.mall.common.service.UserService;
import com.stedu.mall.orderservice.mapper.OrderDetailMapper;
import com.stedu.mall.orderservice.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    //Doubbo 调用服务
    @DubboReference
    private UserService userService;
    @DubboReference
    private GoodsService goodsService;

    @Override
    public void insert(Order order) {
        //生成唯一id
        String id = IdUtil.getSnowflakeNextIdStr();
        order.setId(id);
        orderMapper.insert(order);
        //判断是否有详情要添加
        if (order.getOrderDetailList() != null && order.getOrderDetailList().size() > 0) {
            order.getOrderDetailList()
                    .forEach(detail -> detail.setOrderId(order.getId()));

            orderDetailMapper.insert(order.getOrderDetailList());
        }
    }

    @Override
    public void delete(String id) throws SteduException {
        if (orderMapper.selectById(id) == null) {
            throw new SteduException("该订单不存在，无法删除");
        }

        //删除订单
        orderMapper.delete(id);
        //删除订单详情
        orderDetailMapper.delete(id);
    }

    @Override
    public void update(Order order) {
        Order newInfo = order;
        orderMapper.update(order);
        //清除详情
        orderDetailMapper.delete(newInfo.getId());
        //判断是否要添加详情
        if (newInfo.getOrderDetailList() != null && newInfo.getOrderDetailList().size() > 0) {
            newInfo.getOrderDetailList()
                    .forEach(detail -> detail.setOrderId(newInfo.getId()));
            //添加详情
            orderDetailMapper.insert(newInfo.getOrderDetailList());
        }
    }

    @Override
    public PageInfo<Order> selectByCondition(Order condition, Integer pageNum, Integer pageSize) {
        //设置分页信息
        PageHelper.startPage(pageNum, pageSize);

        //查询
        List<Order> orders = orderMapper.selectByCondition(condition);
        //设置用户
        for (Order order : orders) {
            User user = userService.selectById(order.getUserId());
            order.setUser(user);
        }
        //设置商品
        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetails) {
                Goods goods = goodsService.selectById(orderDetail.getGoodsId());
                orderDetail.setGoods(goods);
            }
            order.setOrderDetailList(orderDetails);
        }
        //设置地址
        for (Order order : orders) {
            Integer addrId = order.getAddrId();
            Addr addr = userService.selectByAddrId(addrId);
            order.setAddr(addr);
        }
        //创建分页信息
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return pageInfo;

    }

    @Override
    public Order selectById(String id) {
        Order order = orderMapper.selectById(id);
        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(id);
        for (OrderDetail orderDetail : orderDetails) {
            Goods goods = goodsService.selectById(orderDetail.getGoodsId());
            orderDetail.setGoods(goods);
        }
        order.setOrderDetailList(orderDetails);

        return order;
    }


}
