package com.stedu.mall.common.service;

import com.stedu.mall.common.bean.Cart;
import com.stedu.mall.common.exception.SteduException;

public interface CartService {
    //添加
    boolean insertOrUpdate(Cart cart);

    //修改
    boolean update(Cart cart) throws SteduException;
}
