package com.stedu.mall.goodsservice;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.service.CategoryService;
import com.stedu.mall.goodsservice.mapper.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GoodsServiceApplicationTests {
    @Autowired
    private CategoryMapper categoryMapper;
    private CategoryService categoryService;

    @Test
    public void test01() {
        System.out.println(categoryMapper.selectById(1));
    }

    @Test
    public void test02() {
        Category c = new Category();
        c.setName("手机");

        PageInfo<Category> pageInfo = (PageInfo<Category>) categoryService.selectByCondition(c, 1, 5);
        pageInfo.getList()
                .stream()
                .forEach(System.out::println);
    }

    @Test
    public void test03() {
        Category c = new Category();
        c.setParentId(0);

        List<Category> categories = (List<Category>) categoryService.selectByCondition(c, null, null);
        categories.stream()
                .forEach(System.out::println);
    }

}
