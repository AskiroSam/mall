package com.stedu.mall.goodsservice;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.exception.SteduException;
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
    @Autowired
    private CategoryService categoryService;

    @Test
    public void test01() {
        System.out.println(categoryMapper.selectById(2));
    }
    @Test
    public void test11() {
        System.out.println(categoryMapper.selectByParentId(2));
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

    //@Test
    //public void test04() throws SteduException {
    //    Category category = new Category(null, "XIXI", "111", "111", 0, 0, 0, null, null);
    //    categoryService.insert(category);
    //}
    //
    //@Test
    //public void test05() throws SteduException {
    //    categoryService.delete(7);
    //}
    //
    //@Test
    //public void test06() throws SteduException {
    //    Category category = new Category(6, "手机", "111", "111", 0, 0, 0, null, null);
    //    categoryService.update(category);
    //}
    //
    //@Test
    //public void test07() {
    //    Category category = new Category(1, "手机", "111", "111", 2, 0, 0, null, null);
    //    categoryMapper.update(category);
    //}
    //
    //@Test
    //public void test08() {
    //    Category category = new Category(1, "手机", "111", "111", 1, 0, 0, null, null);
    //    categoryMapper.update(category);
    //}

}
