package com.stedu.mall.orderservice.mapper;

import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.GoodsPic;
import com.stedu.mall.common.bean.OrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDetailMapper {
    //根据订单id查询
    List<OrderDetail> selectByOrderId(Integer orderId);

    //根据商品id查询
    List<Goods> selectById(Integer goodsId);
}
