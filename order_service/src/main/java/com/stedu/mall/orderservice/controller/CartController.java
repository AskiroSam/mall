package com.stedu.mall.orderservice.controller;

import com.stedu.mall.common.bean.Cart;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.service.CartService;
import com.stedu.mall.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{goodsId}")
    //添加
    public RespBean insert(@PathVariable("goodsId") Integer goodsId, @RequestHeader("token") String token) {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        //创建购物车对象
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setGoodsId(goodsId);

        cartService.insertOrUpdate(cart);

        return RespBean.ok("添加到购物车成功");
    }
}
