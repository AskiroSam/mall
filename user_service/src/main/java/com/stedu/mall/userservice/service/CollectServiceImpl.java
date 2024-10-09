package com.stedu.mall.userservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Collect;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.CollectService;
import com.stedu.mall.userservice.mapper.CollectMapper;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectMapper collectMapper;

    //注意：一个商品只能被一个用户收藏一次
    @Override
    public boolean insert(Collect collect) throws SteduException {
        //根据商品id和用户id查询
        if (collectMapper.selectByGoodsIdAndUserId(collect.getGoodsId(), collect.getUserId()) != null) {
            throw new SteduException("该商品不能被重复收藏");
        }
        //添加
        return collectMapper.insert(collect) == 1;
    }

    @Override
    public boolean delete(Integer id, Integer userId) throws SteduException {
        //判断这个收藏是否存在
        Collect collect = collectMapper.selectById(id);
        if (collect == null) {
            throw new SteduException("该收藏不存在，无法取消收藏");
        }

        //判断这个收藏是否属于当前用户
        if (!collect.getUserId().equals(userId)) {
            throw new SteduException("该收藏属于其它用户，无法取消收藏");
        }

        //删除 - 取消收藏
        return collectMapper.delete(id) == 1;
    }

    @Override
    public Collect selectByGoodsIdAndUserId(Integer goodsId, Integer userId) {
        return collectMapper.selectByGoodsIdAndUserId(goodsId, userId);
    }

    @Override
    public PageInfo<Collect> selectByPage(Integer pageNum, Integer pageSize, Integer userId) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //查询
        List<Collect> collectList = collectMapper.selectByUserId(userId);
        //创建分页信息的对象
        PageInfo<Collect> pageInfo = new PageInfo<>(collectList);
        return pageInfo;
    }
}
