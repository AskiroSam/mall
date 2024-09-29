package com.stedu.mall.orderservice.service;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.Order;
import com.stedu.mall.common.bean.User;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.OrderService;
import com.stedu.mall.common.service.UserService;
import com.stedu.mall.orderservice.mapper.OrderDetailMapper;
import com.stedu.mall.orderservice.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        Integer userId = condition.getUserId();
        User user = userService.selectById(userId);
        condition.setUser(user);
        //查询
        List<Order> orders = orderMapper.selectByCondition(condition);
        //创建分页信息
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return pageInfo;

    }

    @Override
    public Order selectById(String id) {
        return orderMapper.selectById(id);
    }
}
