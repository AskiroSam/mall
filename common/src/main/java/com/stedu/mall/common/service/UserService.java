package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.User;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;



public interface UserService {
    //添加
    boolean insert(User user) throws SteduException;
    //登录
    User login(String username, String password) throws SteduException;
    //注册
    boolean reg(User user) throws SteduException;
    //删除
    boolean delete(Integer id) throws SteduException;
    //修改认证状态
    boolean setStatus(Integer status);
    //修改
    boolean update(User user) throws SteduException;
    //修改密码
    boolean updatePwd(User user) throws SteduException;
    //修改支付密码
    boolean updatePay(User user) throws SteduException;
    //充值金额
    boolean updateInMoney(User user) throws SteduException;
    //提现金额
    boolean updateOutMoney(User user) throws SteduException;
    //根据id查询
    User selectById(Integer id);
    //根据条件查询
    Object selectByCondition(User condition, Integer pageNum, Integer pageSize);
    //查询所有
    List<User> selectAll();
    //查询地址
    Addr selectByAddrId(Integer id);
    //支付
    boolean payUpdate(User user);

}
