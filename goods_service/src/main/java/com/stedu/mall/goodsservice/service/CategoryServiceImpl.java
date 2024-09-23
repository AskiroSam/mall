package com.stedu.mall.goodsservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.exception.SteduException;
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
    public boolean insert(Category category) throws SteduException {
        /*
        * 判断被添加分类对应的夫分类是否存在
        *   1.根据被添加分类的parentId查询其夫分类
        *       1 -- 不存在 -- 抛出异常
        *       2 -- 存在 -- 继续
        *   2.判断这个“夫分类”是否真的是夫分类
        *       1 -- 不是 -- 抛出异常
        *       2 -- 是 -- 继续
        * */
        if (!category.getParentId().equals(0)) { //不是夫分类的情况
            Category parent = categoryMapper.selectById(category.getParentId());
            if (parent == null || (parent != null && !parent.getParentId().equals(0))) {
                throw new SteduException("被添加分类的夫分类不存在或不合法，无法添加");
            }
        }

        /*
        * 判断同一个夫分类下是否有同名的子分类
        *   1.查询同一个夫分类下所有的子分类
        *   2.判断是否重名
        * */
        List<Category> categoryList = categoryMapper.selectByParentId(category.getParentId());
        //判断是否重名
        boolean flag = categoryList.stream()
                .anyMatch(item -> item.getName().equals(category.getName()));
        if (flag) {
            throw new SteduException("被添加分类的名字和同级别其它分类的名字相同，不允许添加");
        }

        //添加
        return categoryMapper.insert(category) == 1;
    }

    @Override
    public boolean delete(Integer id) throws SteduException {
        //判断被删除的分类是否存在
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new SteduException("该分类不存在，无法删除");
        }
        //如果是夫分类判断其下是否有子分类
        if (category.getParentId().equals(0) && category.getChildList().size() > 0) {
            throw new SteduException("该分类存在关联的子分类，无法删除");
        }

        //TODO 如果是子分类判断其下是否有商品

        return categoryMapper.delete(id) == 1;
    }

    @Override
    public boolean update(Category category) throws SteduException { // category内容可能不全
        //查询被修改分类的详细信息（根据id查询） - 修改之前的详细信息
        Category oldInfo = categoryMapper.selectById(category.getId());
        Category newInfo = category;

        //判断被修改的分类是否存在
        if (oldInfo == null) {
            throw new SteduException("该分类不存在，无法修改");
        }

        //如果该分类是一个夫分类，并且其下存在子分类，那么这个夫分类就不能被修改为子分类
        if (oldInfo.getParentId().equals(0) && oldInfo.getChildList().size() > 0 && (newInfo.getParentId() != null && !newInfo.getParentId().equals(0))) {
            throw new SteduException("该分类存在子分类，无法修改为子分类");
        }

        //一个夫分类不能把自己设置为自己的夫分类
        if (oldInfo.getParentId().equals(0) && !newInfo.getParentId().equals(0) && newInfo.getParentId().equals(oldInfo.getId())) {
            throw new SteduException("不能设置当前分类为自己的夫分类，无法修改");
        }

        /*
        * 判断同一个夫分类下是否有同名的子分类
        *   1.查询同一个夫分类下所有的子分类
        *   2.判断是否重名
        * */
        //获取修改之后的夫分类的id
        Integer parentId = newInfo.getParentId() == null ? oldInfo.getParentId() : newInfo.getParentId();
        //获取修改之后的名字
        String name = newInfo.getName() == null ? oldInfo.getName() : newInfo.getName();
        //查询修改之后同一个夫分类下所有的子分类
        if (categoryMapper.selectByParentId(parentId)
                .stream()
                .anyMatch(item -> item.getName().equals(name) && !item.getId().equals(category.getId()))) {
            throw new SteduException("修改之后的分类名和同级别的其它分类名相同，不允许修改");
        }

        //修改
        return categoryMapper.update(category) == 1;
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
