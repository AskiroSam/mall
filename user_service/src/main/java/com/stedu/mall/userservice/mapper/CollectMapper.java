package com.stedu.mall.userservice.mapper;

import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.bean.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectMapper {
    //添加
    int insert(Collect collect);
    //删除
    int delete(Integer id);
    //根据id查询
    Collect selectById(Integer id);
    //根据商品id和用户id查询
    Collect selectByGoodsIdAndUserId(@Param("goodsId") Integer goodsId, @Param("userId") Integer userId);
    //根据条件查询
    List<Collect> selectByCondition(Collect condition);

    List<Collect> selectByUserId(Integer userId);
}
