package com.stedu.mall.goodsservice;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.GoodsPic;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.CategoryService;
import com.stedu.mall.goodsservice.mapper.CategoryMapper;
import com.stedu.mall.goodsservice.mapper.GoodsMapper;
import com.stedu.mall.goodsservice.mapper.GoodsPicMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GoodsServiceApplicationTests2 {
    @Autowired
    private GoodsPicMapper goodsPicMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Test
    public void test01() {
        goodsPicMapper.selectByGoodsId(4)
                .stream()
                .forEach(System.out::println);
    }

    @Test
    public void test02() {
        goodsPicMapper.delete(1000);
    }

    @Test
    public void test03() {
        ArrayList<GoodsPic> goodsPics  = new ArrayList<>();
        goodsPics.add(new GoodsPic(null, "1111", 1000));

        goodsPicMapper.insert(goodsPics);
    }

    @Test
    public void test04() {
        System.out.println(goodsMapper.selectById(4));
    }

    @Test
    public void test05() {
        Goods condition = new Goods();
        condition.setName("华为");

        goodsMapper.selectByCondition(condition)
                .stream()
                .forEach(System.out::println);
    }

    @Test
    public void test06() {
        Goods goods = new Goods(null, "qqq", "aaa", "aaa", new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), "1", "1", 0, 0, 3, 0F, 1, null, null);
        goodsMapper.insert(goods);
    }

    @Test
    public void test07() {

    }

}
