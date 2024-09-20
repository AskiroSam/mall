package com.stedu.mall.goodsservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.service.CategoryService;
import com.stedu.mall.goodsservice.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public int insert(Category category) {
        return 0;
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }

    @Override
    public int update(Category category) {
        return 0;
    }

    /*
    * 分页和不分页都可以使用
    * */
    @Override
    public Object selectByCondition(Category condition, Integer pageNum, Integer pageSize) {
        if (pageNum == null) { //不分页
            return categoryMapper.selectByCondition(condition);
        } else { //分页
            //设置分页参数
            PageHelper.startPage(pageNum, pageSize);
            //查询
            List<Category> categories = categoryMapper.selectByCondition(condition);
            //创建分页信息的对象
            PageInfo<Category> pageInfo = new PageInfo<>(categories);

            return pageInfo;
        }
    }

    @Override
    public Category selectBtyId(Integer id) {
        return categoryMapper.selectById(id);
    }
}
