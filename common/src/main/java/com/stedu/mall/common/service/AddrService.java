package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;


public interface AddrService {
    //添加
    int insert(Addr addr);
    //根据id删除
    int delete(Integer id, Integer userId) throws SteduException;
    //根据id修改
    int update(Addr addr, Integer userId) throws SteduException;
    //根据id查询
    Addr selectById(Integer id);
    //根据条件查询
    List<Addr> selectByCondition(Addr condition);
}
