package com.stedu.mall.userservice.mapper;

import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    //添加
    int insert(User user);
    //根据id删除
    int delete(Integer id);
    //修改状态
    int setStatus(@Param("id") Integer id, @Param("status") Integer status);
    //根据id修改
    int update(User user);
    //根据id查询
    User selectById(Integer id);
    //根据条件查询
    List<User> selectByCondition(User condition);
}
