package com.stedu.mall.goodsservice;

import com.stedu.mall.goodsservice.mapper.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GoodsServiceApplicationTests {
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void test01() {
        categoryMapper.selectById(1);
    }

}
