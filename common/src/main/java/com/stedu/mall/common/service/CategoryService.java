package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Category;

import java.util.List;

public interface CategoryService {
    //添加
    int insert(Category category);
    //删除
    int delete(Integer id);
    //修改
    int update(Category category);
    //条件搜索 - 分页/不分页
    Object selectByCondition(Category condition, Integer pageNum, Integer pageSize);
    //根据id查询
    Category selectBtyId(Integer id);
}
