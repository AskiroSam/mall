package com.stedu.mall.orderservice.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
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
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @GlobalTransactional(rollbackFor = Exception.class)
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

        orderVo.setOrderId(orderId);

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
    public Order selectById(String id, Integer userId) throws SteduException {
        //根据订单id查询
        Order order = orderMapper.selectById(id);
        //判断是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            throw new SteduException("订单不属于当前用户，无法支付");
        }
        //查询订单详情里面的商品信息
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

    //TODO 事务处理
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void pay(OrderVo orderVo) throws SteduException {
        Order order = orderMapper.selectById(orderVo.getOrderId());

        if (order == null) {
            throw new SteduException("订单不存在，无法支付");
        }

        if (!order.getUserId().equals(orderVo.getUserId())) {
            throw new SteduException("订单不属于当前用户，无法支付");
        }

        if (!order.getStatus().equals(0)) {
            throw new SteduException("订单状态错误，无法支付");
        }

        order.setStatus(1);
        order.setPayType(0);
        orderMapper.update(order);

        User user = userService.selectById(orderVo.getUserId());
        //判断支付密码是否正确
        String payPwdMySQL = user.getPayPassword();
        if (payPwdMySQL == null) {
            throw new SteduException("用户没有设置支付密码，请设置之后再下单");
        }
        //对用户输入的支付密码进行加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(orderVo.getPayPwd() + user.getSalt()));
        if (!md5Pwd.equals(payPwdMySQL)) {
            throw new SteduException("支付密码错误，无法支付");
        }
        //计算当前订单的总金额
        BigDecimal sum = new BigDecimal("0");
        for (OrderDetail orderDetail : order.getOrderDetailList()) {
            BigDecimal count = new BigDecimal(orderDetail.getCount());
            BigDecimal price = orderDetail.getPrice();
            sum = sum.add(price.multiply(count));
        }
        //判断余额是否充足 BigDecimal比较大小 - java比较器(BigDecimal实现了自然排序)
        if (user.getMoney().compareTo(sum) < 0) {
            throw new SteduException("余额不足，无法支付");
        }
        //扣除余额
        User u = new User();
        u.setId(user.getId());
        u.setMoney(user.getMoney().subtract(sum));
        userService.payUpdate(u);

    }

}
