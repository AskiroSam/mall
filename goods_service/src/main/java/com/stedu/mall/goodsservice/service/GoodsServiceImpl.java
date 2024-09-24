package com.stedu.mall.goodsservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.GoodsService;
import com.stedu.mall.goodsservice.mapper.CategoryMapper;
import com.stedu.mall.goodsservice.mapper.GoodsMapper;
import com.stedu.mall.goodsservice.mapper.GoodsPicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsPicMapper goodsPicMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    /*
    * 添加
    *   1.所属分类是否存在，并且必须是子分类
    *   2.不能添加同名的商品
    *       -分类
    *       -名字
    *       -颜色
    *       -版本
    *   3.添加商品 -- 主键回填
    *   4.添加商品图片
    * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(Goods goods) throws SteduException {
        //所属分类是否存在，并且必须是子分类
        Category category = categoryMapper.selectById(goods.getCategoryId());
        if (category == null || (category != null && category.getParentId().equals(0))) {
            throw new SteduException("所属的分类不存在，或分类是一个夫分类无法添加");
        }

        //不能添加同名的商品 - 查找是否存在同名的商品
        Goods condition = new Goods();
        condition.setCategoryId(goods.getCategoryId());
        condition.setName(goods.getName());
        condition.setColor(goods.getColor());
        condition.setVersion(goods.getVersion());

        if (goodsMapper.selectByCondition(condition)
                .stream()
                //此时图片是模糊查询，需要精确匹配名字
                .anyMatch(item -> item.getName().equals(goods.getName()))) {
            throw new SteduException("存在相同的商品，无法添加");
        }

        //添加商品
        goodsMapper.insert(goods);

        //判断是否有图片需要添加
        if (goods.getPicList() != null && goods.getPicList().size() > 0) {
            //设置图片的商品id
            goods.getPicList()
                    .forEach(pic -> pic.setGoodsId(goods.getId()));
            //添加图片
            goodsPicMapper.insert(goods.getPicList());
        }
    }

    /*
    * 删除
    *   1.判断商品是否存在
    *   2.判断是否关联订单
    *   3.删除商品图片
    *   4.删除商品
    * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) throws SteduException {
        //判断商品是否存在
        if (goodsMapper.selectById(id) == null) {
            throw new SteduException("该商品不存在，无法删除");
        }

        //TODO 判断是否关联订单

        //删除商品图片
        goodsPicMapper.delete(id);
        //删除商品
        goodsMapper.delete(id);
    }

    /*
    * 修改
    *   1.判断商品是否存在
    *   2.判断修改之后的分类是否存在并且必须是子分类 - 前提是必须修改分类
    *   3.不能同名
    *       -分类
    *       -名字
    *       -颜色
    *       -版本
    *   4.修改商品信息
    *   5.修改图片
    * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Goods goods) throws SteduException {
        Goods newInfo = goods;
        Goods oldInfo = goodsMapper.selectById(goods.getId());
        //判断商品是否存在
        if (oldInfo == null) {
            throw new SteduException("该商品不存在，无法修改");
        }

        //判断是否修改了分类
        if (newInfo.getCategoryId() != null && !newInfo.getCategoryId().equals(oldInfo.getCategoryId())) {
            //判断修改之后的分类是否存在并且必须是子分类
            Category category = categoryMapper.selectById(newInfo.getCategoryId());
            if (category == null || (category != null && category.getParentId() == 0)) {
                throw new SteduException("修改之后的分类不存在或修改之后的分类是一个夫分类，无法修改");
            }
        }

        //不能同名 分类 名字 颜色 版本
        Integer newCategoryId = newInfo.getCategoryId() == null ? oldInfo.getCategoryId() : newInfo.getCategoryId();
        String newName = newInfo.getName() == null ? oldInfo.getName() : newInfo.getName();
        String newColor = newInfo.getColor() == null ? oldInfo.getColor() : newInfo.getColor();
        String newVersion = newInfo.getVersion() == null ? oldInfo.getVersion() : newInfo.getVersion();
        Goods condition = new Goods();
        condition.setCategoryId(newCategoryId);
        condition.setName(newName);
        condition.setColor(newColor);
        condition.setVersion(newVersion);

        if (goodsMapper.selectByCondition(condition)
                .stream()
                .anyMatch(item -> item.getName().equals(newName) && !item.getId().equals(newInfo.getId()))) {
            throw new SteduException("修改之后的信息和其它商品信息相同，无法修改");
        }

        //修改商品信息
        goodsMapper.update(goods);
        //修改图片 1.删除之前的图片 2.添加新的图片
        goodsPicMapper.delete(newInfo.getId());
        if (newInfo.getPicList() != null && newInfo.getPicList().size() > 0) {
            //设置每个图片的商品id
            newInfo.getPicList().stream()
                    .forEach(pic -> pic.setGoodsId(newInfo.getId()));
            //添加图片
            goodsPicMapper.insert(newInfo.getPicList());
        }
    }

    @Override
    public Goods selectById(Integer id) {
        return goodsMapper.selectById(id);
    }

    @Override
    public PageInfo<Goods> selectByCondition(Goods condition, Integer pageNum, Integer pageSize) {
        //设置分页信息
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<Goods> goods = goodsMapper.selectByCondition(condition);
        //创建分页信息
        PageInfo<Goods> pageInfo = new PageInfo<>(goods);
        return pageInfo;

    }
}
