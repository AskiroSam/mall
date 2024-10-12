package com.stedu.mall.userservice.controller;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.bean.Collect;
import com.stedu.mall.common.bean.Order;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.CollectService;
import com.stedu.mall.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectService collectService;

    @PostMapping("/{goodsId}")
    //添加 -- 收藏
    public RespBean insert(@PathVariable("goodsId") Integer goodsId, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        //创建收藏对象
        Collect collect = new Collect();
        collect.setGoodsId(goodsId);
        collect.setUserId(userId);
        //添加
        collectService.insert(collect);

        return RespBean.ok("收藏成功");
    }

    @DeleteMapping("/{id}")
    //删除 -- 取消收藏
    public RespBean delete(@PathVariable("id") Integer id, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        collectService.delete(id, userId);

        return RespBean.ok("取消收藏成功");
    }

    @GetMapping("/{goodsId}")
    //根据商品id和用户id查询收藏
    public RespBean selectByGoodsIdAndUserId(@PathVariable("goodsId") Integer goodsId, @RequestHeader("token") String token) {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        Collect collect = collectService.selectByGoodsIdAndUserId(goodsId, userId);

        return RespBean.ok("", collect);
    }

    @GetMapping
    public RespBean selectByPage(Collect condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Collect> pageInfo = collectService.selectByCondition(condition, pageNum, pageSize);

        return RespBean.ok("查询成功", pageInfo);
    }


}
