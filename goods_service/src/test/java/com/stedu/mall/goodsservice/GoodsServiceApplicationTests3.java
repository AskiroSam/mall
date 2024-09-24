package com.stedu.mall.goodsservice;

import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.GoodsPic;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.GoodsService;
import com.stedu.mall.goodsservice.mapper.GoodsMapper;
import com.stedu.mall.goodsservice.mapper.GoodsPicMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootTest
class GoodsServiceApplicationTests3 {
    @Autowired
    private GoodsService goodsService;
    @Test
    public void test01() throws SteduException {
        Goods goods = new Goods(null, "qqq", "aaa", "aaa", new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), "2", "1", 0, 0, 3, 0F, 1, null, null);
        goodsService.insert(goods);
    }

    @Test
    public void test02() throws SteduException {
        Goods goods = new Goods();
        goods.setId(508);
        goods.setColor("2");
        goods.setCategoryId(3);
        goodsService.update(goods);
    }

}
