package com.stedu.mall.goodsservice;

import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.GoodsService;
import com.stedu.mall.common.config.WhiteListConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class GoodsServiceApplicationTests3 {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private WhiteListConfig whiteListConfig;

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

    @Test
    public void test03() {
        whiteListConfig.getRules()
                .stream()
                .forEach(System.out::println);
    }

    @Test
    public void test04() {
        String path1 = "/goods/1";


        //要求：以/goods开始
        String reg = "^/goods.*";
        System.out.println(path1.matches(reg));
    }

    @Test
    public void test05() {
        String reg = "^/category/[\\d]+";
        System.out.println("/category/1".matches(reg));
        System.out.println("/category/11".matches(reg));

    }

}
