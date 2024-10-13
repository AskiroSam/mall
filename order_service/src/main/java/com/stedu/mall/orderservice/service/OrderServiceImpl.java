package com.stedu.mall.orderservice.service;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.*;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.AddrService;
import com.stedu.mall.common.service.GoodsService;
import com.stedu.mall.common.service.OrderService;
import com.stedu.mall.common.service.UserService;
import com.stedu.mall.orderservice.mapper.CartMapper;
import com.stedu.mall.orderservice.mapper.OrderDetailMapper;
import com.stedu.mall.orderservice.mapper.OrderMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private CartMapper cartMapper;
    //Doubbo 调用服务
    @DubboReference
    private UserService userService;
    @DubboReference
    private AddrService addrService;
    @DubboReference
    private GoodsService goodsService;

    @Override
    public void insert(OrderVo orderVo) throws SteduException {
        /*
        * 1.生成订单
        *   1）生成订单ID - 雪花算法生成
        *   2）获取地址对象
        *   3）设置地址详情
        *   4）添加订单
        * 2.生成详情
        *   1）获取购物车 - 判断购物车是否合法
        *   2）获取商品信息
        *       是否存在
        *       是否下架
        *       库存是否充足
        * 3.删除购物车
        * */
        //创建订单对象
        Order order = new Order();
        //生成订单id - 雪花算法生成
        String orderId = IdUtil.getSnowflakeNextIdStr();
        //设置订单id
        order.setId(orderId);

        //获取地址对象
        Addr addr = addrService.selectById(orderVo.getAddrId());
        //设置地址详情
        order.setAddrDetail(addr.getContact() + " " +
                addr.getPhone() + " " +
                addr.getProvince() +
                addr.getCity() +
                addr.getDistrict() +
                addr.getStreet() +
                addr.getAddress());
        //生成订单
        order.setUserId(orderVo.getUserId());
        order.setPayType(0);
        order.setAddrId(orderVo.getAddrId());
        order.setStatus(0);
        orderMapper.insert(order);

        //生成详情 每个购物车的id对应一个商品的详情
        for (Integer cartId : orderVo.getCartIds()) {
            //1)获取购物车 - 判断购物车是否合法
            Cart cart = cartMapper.selectById(cartId);
            if (cart == null) {
                throw new SteduException("购物车不存在，无法生成订单");
            }

            if (!cart.getUserId().equals(orderVo.getUserId())) {
                throw new SteduException("购物车不合法，无法生成订单");
            }

            //2）获取商品信息
            Goods goods = goodsService.selectById(cart.getGoodsId());
            //是否存在
            if (goods == null) {
                throw new SteduException("商品不存在，无法生成订单");
            }
            //是否下架
            if (goods.getStatus().equals(0)) {
                throw new SteduException("商品下架，无法生成订单");
            }
            //库存是否充足
            if (goods.getCount() < cart.getCount()) {
                throw new SteduException("商品库存不足，无法生成订单");
            }

            //3)减去商品库存
            goods.setCount(goods.getCount() - cart.getCount());
            goodsService.update(goods);

            //4)添加详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setCount(cart.getCount());
            orderDetail.setPrice(goods.getPrice());
            orderDetail.setOrderId(orderId);
            orderDetail.setGoodsId(goods.getId());
            orderDetailMapper.insert1(orderDetail);
        }

        //5)删除购物车
        cartMapper.deleteIdList(Arrays.asList(orderVo.getCartIds()));

        ////生成唯一id
        //String id = IdUtil.getSnowflakeNextIdStr();
        //order.setId(id);
        //orderMapper.insert(order);
        ////判断是否有详情要添加
        //if (order.getOrderDetailList() != null && order.getOrderDetailList().size() > 0) {
        //    order.getOrderDetailList()
        //            .forEach(detail -> detail.setOrderId(order.getId()));
        //
        //    orderDetailMapper.insert(order.getOrderDetailList());
        //}
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

    @Override
    public List<Order> selectAll() {
        return orderMapper.selectAll();
    }


}
