package com.stedu.mall.goodsservice.mapper;

import com.stedu.mall.common.bean.Goods;

import java.util.List;

public interface GoodsMapper {
    //添加
    int insert(Goods goods);
    //根据id删除
    int delete(Integer id);
    //根据i修改
    int update(Goods goods);
    //根据条件查询
    List<Goods> selectByCondition(Goods condition);
    //根据id查询
    Goods selectById(Integer id);
    //根据分类的id查询商品 -- 这个分类id可能是夫分类的id也可能是子分类的id
    List<Goods> selectByCategoryIdGoods(Integer categoryId);
}
