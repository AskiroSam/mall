package com.stedu.mall.userservice.mapper;

import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.bean.User;

import java.util.List;

public interface AddrMapper {
    //添加
    int insert(Addr addr);
    //根据id删除
    int delete(Integer id);
    //修改状态
    int setStatus(Integer status);
    //根据id修改
    int update(Addr addr);
    //根据id查询
    Addr selectById(Integer id);
    //根据条件查询
    List<Addr> selectByCondition(Addr condition);
    //查询所有
    List<Addr> selectAll();
}

