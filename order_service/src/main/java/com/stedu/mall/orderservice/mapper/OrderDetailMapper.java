package com.stedu.mall.orderservice.mapper;

import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.GoodsPic;
import com.stedu.mall.common.bean.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDetailMapper {
    //根据订单id查询
    List<OrderDetail> selectByOrderId(String orderId);

    //根据商品id查询
    List<Goods> selectByGoodsId(Integer goodsId);

    //添加
    int insert(@Param("orderDetailList") List<OrderDetail> orderDetailList);
    //添加
    int insert1(OrderDetail orderDetail);
    //根据订单id删除
    int delete(String orderId);
    //修改详情
    int update(OrderDetail orderDetail);
}
