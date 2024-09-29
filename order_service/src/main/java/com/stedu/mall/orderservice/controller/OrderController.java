package com.stedu.mall.orderservice.controller;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.Order;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //添加
    @PostMapping
    public RespBean insert(@RequestBody Order order) throws SteduException {
        orderService.insert(order);

        return RespBean.ok("添加成功");
    }
    //删除
    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") String id) throws SteduException {
        orderService.delete(id);

        return RespBean.ok("删除成功");
    }


    //修改
    @PutMapping
    public RespBean update(@RequestBody Order order) throws SteduException {
        orderService.update(order);

        return RespBean.ok("修改成功");
    }

    //搜索
    @GetMapping
    public RespBean selectByPage(Order condition, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        PageInfo<Order> pageInfo = orderService.selectByCondition(condition, pageNum, pageSize);

        return RespBean.ok("查询成功", pageInfo);
    }

    //根据id查询
    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") String id) {
        Order order = orderService.selectById(id);

        return RespBean.ok("查询成功", order);
    }
}
