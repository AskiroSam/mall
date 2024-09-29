package com.stedu.mall.goodsservice.controller;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    //添加
    @PostMapping
    public RespBean insert(@RequestBody Goods goods) throws SteduException {
        goods.setScore(5F);
        goodsService.insert(goods);

        return RespBean.ok("添加成功");
    }
    //删除
    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id) throws SteduException {
        goodsService.delete(id);

        return RespBean.ok("删除成功");
    }


    //修改
    @PutMapping
    public RespBean update(@RequestBody Goods goods) throws SteduException {
        goodsService.update(goods);

        return RespBean.ok("修改成功");
    }

    //修改是否推荐
    @PutMapping("/chgRecom")
    public RespBean chgRecom(@RequestBody Goods goods) throws SteduException {
        goodsService.chgRecom(goods);
        return RespBean.ok("修改成功");
    }

    //修改是否上架
    @PutMapping("/chgStatus")
    public RespBean chgStatus(@RequestBody Goods goods) throws SteduException {
        goodsService.chgStatus(goods);
        return RespBean.ok("修改成功");
    }


    //搜索
    @GetMapping
    public RespBean selectByPage(Goods condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Goods> pageInfo = goodsService.selectByCondition(condition, pageNum, pageSize);

        return RespBean.ok("查询成功", pageInfo);
    }

    //根据id查询
    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        Goods goods = goodsService.selectById(id);

        return RespBean.ok("查询成功", goods);
    }
}
