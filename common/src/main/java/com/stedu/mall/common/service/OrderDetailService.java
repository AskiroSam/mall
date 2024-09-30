package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDetailService {


    //根据商品id查询
    List<Goods> selectByGoodsId(Integer goodsId);

    //添加
    void insert(@Param("orderDetailList") List<OrderDetail> orderDetailList);
    //根据订单id删除
    void delete(String orderId);
    //修改详情
    void update(OrderDetail orderDetail);
}
