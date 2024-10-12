package com.stedu.mall.orderservice.service;

import com.stedu.mall.common.bean.Cart;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.CartService;
import com.stedu.mall.common.service.GoodsService;
import com.stedu.mall.orderservice.mapper.CartMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public  class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @DubboReference
    private GoodsService goodsService;

    @Override
    public boolean insertOrUpdate(Cart cart) {
        //先查询购物车中是否存在该记录 1.存在 - 累加  2.不存在 - 添加
        Cart c = cartMapper.selectByUserIdAndGoodsId(cart.getUserId(), cart.getGoodsId());

        if (c == null) {
            //添加
            cart.setCount(1);
            return cartMapper.insert(cart) == 1;
        } else {
            //修改 - 累加
            c.setCount(c.getCount() + 1);
            return cartMapper.update(c) == 1;
        }

    }

    //单选删除
    @Override
    public boolean delete(Integer id, Integer userId) throws SteduException {
        //判断购物车是否存在
        Cart cart = cartMapper.selectById(id);
        if (cart == null) {
            throw new SteduException("该购物车不存在，无法删除");
        }
        //判断被删除的购物车是否属于当前的用户
        if (!cart.getUserId().equals(userId)) {
            throw new SteduException("该购物车属于其他用户，无法删除");
        }
        //删除
        return cartMapper.delete(id) == 1;
    }

    //多选删除
    @Override
    public boolean deleteIdList(List<Integer> idList, Integer userId) throws SteduException {
        for (Integer id : idList) {
            Cart cart = cartMapper.selectById(id);
            //判断选中的商品是否存在
            if (cart == null) {
                throw new SteduException("该购物车不存在，无法删除");
            }

            //判断被修改的购物车是否属于当前用户
            if (!cart.getUserId().equals(userId)) {
                throw new SteduException("该购物车属于其他用户，无法修改");
            }
        }

        //删除
        return cartMapper.deleteIdList(idList) == idList.size();
    }

    @Override
    public boolean update(Cart cart, Integer userId) throws SteduException {
        //判断该购物车是否存在
        Cart c = cartMapper.selectById(cart.getId());
        if (c == null) {
            throw new SteduException("该购物车记录不存在，无法修改");
        }

        //判断被修改的购物车是否属于当前用户
        if (!c.getUserId().equals(userId)) {
            throw new SteduException("该购物车属于其他用户，无法修改");
        }

        //查询购物车对应的商品
        Goods goods = goodsService.selectById(c.getGoodsId());
        if (cart.getCount() != null && cart.getCount() > goods.getCount()) {
            //当购物商品的数量超过库存，就设置购物车商品数量为库存
            cart.setCount(goods.getCount());
        }

        return cartMapper.update(cart) == 1;
    }

    @Override
    public List<Cart> search(Cart condition) {
        List<Cart> cartList = cartMapper.selectByCondition(condition);
        //调用goods_service查询购物车对应的商品
        cartList.stream()
                .forEach(cart -> {
                    //查询对应的商品信息
                    Goods goods = goodsService.selectById(cart.getGoodsId());
                    cart.setGoods(goods);
                });
        //for (Cart cart : cartList) {
        //    Goods goods = goodsService.selectById(cart.getGoodsId());
        //    cart.setGoods(goods);
        //}

        return cartList;
    }

    @Override
    public Cart selectById(Integer id) {
        return cartMapper.selectById(id);
    }

    @Override
    public List<Cart> selectByIds(Integer[] cartIds, Integer userId) throws SteduException {
        List<Cart> cartList = cartMapper.selectByIds(cartIds);

        //判断是否属于当前用户
        for (Cart cart : cartList) {
            if (!cart.getUserId().equals(userId)) {
                throw new SteduException("非法的购物车");
            }

            //查询并设置购物车的商品信息
            Goods goods = goodsService.selectById(cart.getGoodsId());
            cart.setGoods(goods);
        }

        return cartList;
    }
}
