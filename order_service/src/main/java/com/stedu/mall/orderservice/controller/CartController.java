package com.stedu.mall.orderservice.controller;

import com.stedu.mall.common.bean.Cart;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.CartService;
import com.stedu.mall.common.utils.JwtUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    //@DeleteMapping
    //public RespBean deleteIdList(@RequestBody List<Integer> idList, @RequestHeader("token") String token) throws SteduException {
    //    //解析token获取用户id
    //    Map<String, Object> map = JwtUtils.parseJwtToMap(token);
    //    Integer userId = (Integer)map.get("id");
    //
    //    cartService.deleteIdList(idList, userId);
    //
    //    return RespBean.ok("删除成功");
    //}
    @DeleteMapping
    public RespBean deleteIList(@RequestBody Map<String, ArrayList<Integer>> data, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        ArrayList<Integer> idList = data.get("idList");

        cartService.deleteIdList(idList, userId);

        return RespBean.ok("删除成功");
    }

    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        cartService.delete(id, userId);

        return RespBean.ok("删除成功");
    }

    @PutMapping
    public RespBean update(@RequestBody Cart cart, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        cartService.update(cart, userId);

        return RespBean.ok("修改购物车成功");
    }

    //查询已登录用户所有的购物车信息
    @GetMapping
    public RespBean selectCartList(@RequestHeader("token") String token) {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        Cart condition = new Cart();
        condition.setUserId(userId);

        List<Cart> cartList = cartService.search(condition);

        return RespBean.ok("", cartList);
    }

    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        Cart cart = cartService.selectById(id);

        return RespBean.ok("", cart);
    }

    @GetMapping("/ids")
    public RespBean selectByIds(Integer[] cartIds, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        List<Cart> cartList = cartService.selectByIds(cartIds, userId);

        return RespBean.ok("查询成功",cartList);
    }
}
