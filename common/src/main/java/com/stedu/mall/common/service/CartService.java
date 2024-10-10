package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Cart;
import com.stedu.mall.common.exception.SteduException;

import java.util.List;

public interface CartService {
    //添加
    boolean insertOrUpdate(Cart cart);
    //删除
    boolean delete(Integer id, Integer userId) throws SteduException;
    //修改
    boolean update(Cart cart, Integer userId) throws SteduException;
    //搜索
    List<Cart> search(Cart condition);
}
