package com.stedu.mall.common.service;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.bean.Collect;
import com.stedu.mall.common.bean.Order;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;

public interface CollectService {
    //添加
    boolean insert(Collect collect) throws SteduException;
    //删除 - 只能删除自己的收藏
    boolean delete(Integer id, Integer userId) throws SteduException;
    //根据goodsId和userId查询
    Collect selectByGoodsIdAndUserId(Integer goodsId, Integer userId);
    //根据条件查询
    PageInfo<Collect> selectByCondition(Collect condition, Integer pageNum, Integer pageSize);
    //查询所有
    PageInfo<Collect> selectByPage(Integer pageNum, Integer pageSize, Integer userId);
    //根据用户id查询
    List<Collect> selectByUserId(Integer userId);
}
