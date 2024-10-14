package com.stedu.mall.userservice.mapper;

import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    //添加
    int insert(User user);
    //添加 - 为注册服务
    int insert1(User user);
    //根据用户名·查询 - 为登录服务
    User selectByUserName(String username);
    //根据id删除
    int delete(Integer id);
    //修改状态
    int setStatus(Integer status);
    //根据id修改
    int update(User user);
    //修改密码
    int updatePwd(User user);
    //修改支付密码
    int updatePay(User user);
    //修改余额
    int updateMoney(User user);
    //支付
    int payUpdate(User user);
    //根据id查询 - 为获取用户的信息服务
    User selectById(Integer id);
    //根据条件查询
    List<User> selectByCondition(User condition);
    //查询所有
    List<User> selectAll();


}
