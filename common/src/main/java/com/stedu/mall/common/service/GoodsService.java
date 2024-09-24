package com.stedu.mall.common.service;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.exception.SteduException;

public interface GoodsService {
    //添加
    void insert(Goods goods) throws SteduException;
    //根据id删除
    void delete(Integer id) throws SteduException;
    //根据id修改
    void update(Goods goods) throws SteduException;
    //根据id查询
    Goods selectById(Integer id);
    //搜索 - 分页查询
    PageInfo<Goods> selectByCondition(Goods condition, Integer pageNum, Integer pageSize);
}
