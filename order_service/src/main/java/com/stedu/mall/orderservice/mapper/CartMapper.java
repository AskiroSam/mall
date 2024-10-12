package com.stedu.mall.orderservice.mapper;

import com.stedu.mall.common.bean.Cart;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.OrderDetail;
import org.apache.dubbo.auth.v1alpha1.Ca;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    //添加
    int insert(Cart cart);
    //根据id进行删除
    int delete(Integer id);
    //多选删除
    int deleteIdList(@Param("idList") List<Integer> idList);
    //修改
    int update(Cart cart);
    //根据用户id和商品id查询
    Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
    //根据id进行查询
    Cart selectById(Integer id);
    //根据条件进行查询
    List<Cart> selectByCondition(Cart condition);

    List<Cart> selectByIds(@Param("cartIds") Integer[] cartIds);
}
