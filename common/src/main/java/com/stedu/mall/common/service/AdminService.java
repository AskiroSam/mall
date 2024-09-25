package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;

public interface AdminService {
    //添加
    boolean insert(Admin admin);
    //删除
    boolean delete(Integer id);
    //修改可用状态
    boolean setStatus(Integer id);
    //修改
    boolean update(Admin admin) throws SteduException;
    //根据id查询
    Admin selectById(Integer id);
    //根据条件查询
    Object selectByCondition(Admin condition, Integer pageNum, Integer pageSize);

}
