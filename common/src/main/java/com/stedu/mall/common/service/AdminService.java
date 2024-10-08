package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;

public interface AdminService {
    //添加
    boolean insert(Admin admin) throws SteduException;
    //删除
    boolean delete(Integer id) throws SteduException;
    //修改可用状态
    boolean setStatus(Integer id);
    //修改个人信息
    boolean update(Admin admin) throws SteduException;
    //修改密码
    boolean chgPwd(String oldPwd, String newPwd, Integer id) throws SteduException;
    //根据id查询
    Admin selectById(Integer id);
    //根据条件查询
    Object selectByCondition(Admin condition, Integer pageNum, Integer pageSize);

    //登录
    Admin login(String username, String password) throws SteduException;

    //重置密码
    boolean resetPwd(Integer id);

    //查询所有
    List<Admin> selectAll();
}
