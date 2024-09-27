package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.User;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;

public interface UserService {
    //添加
    boolean insert(User user) throws SteduException;
    //删除
    boolean delete(Integer id);
    //修改认证状态
    boolean setStatus(Integer status);
    //修改
    boolean update(User user) throws SteduException;
    //根据id查询
    User selectById(Integer id);
    //根据条件查询
    Object selectByCondition(User condition, Integer pageNum, Integer pageSize);
    //查询所有
    List<User> selectAll();
}
