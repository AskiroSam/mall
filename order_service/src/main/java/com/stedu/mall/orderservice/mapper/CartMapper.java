package com.stedu.mall.orderservice.mapper;

import com.stedu.mall.common.bean.Cart;
import org.apache.dubbo.auth.v1alpha1.Ca;
import org.apache.ibatis.annotations.Param;

public interface CartMapper {
    //添加
    int insert(Cart cart);
    //修改
    int update(Cart cart);
    //根据用户id和商品id查询
    Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
    //根据id进行查询
    Cart selectById(Integer id);
}
