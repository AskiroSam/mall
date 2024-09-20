package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;

public interface CategoryService {
    //添加
    boolean insert(Category category) throws SteduException;
    //删除
    boolean delete(Integer id) throws SteduException;
    //修改
    boolean update(Category category) throws SteduException;
    //条件搜索 - 分页/不分页
    Object selectByCondition(Category condition, Integer pageNum, Integer pageSize);
    //根据id查询
    Category selectBtyId(Integer id);
}
