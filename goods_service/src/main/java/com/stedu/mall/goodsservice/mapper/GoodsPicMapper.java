package com.stedu.mall.goodsservice.mapper;

import com.stedu.mall.common.bean.GoodsPic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsPicMapper {
    //添加
    int insert(@Param("picList") List<GoodsPic> picList);
    //根据商品id删除
    int delete(Integer goodsId);
    //根据商品id查询
    List<GoodsPic> selectByGoodsId(Integer goodsId);
}
