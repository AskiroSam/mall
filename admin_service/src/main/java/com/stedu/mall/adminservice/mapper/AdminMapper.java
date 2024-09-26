package com.stedu.mall.adminservice.mapper;

import com.stedu.mall.common.bean.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    //添加
    int insert(Admin admin);
    //根据id删除
    int delete(Integer id);
    //修改可用状态
    int setStatus(@Param("id") Integer id, @Param("status") Integer status);
    //根据id修改
    int update(Admin admin);
    //根据id查询
    Admin selectById(Integer id);
    //根据条件查询
    List<Admin> selectByCondition(Admin condition);

    //根据用户名进行查询
    Admin selectByUserName(String userName);
}
