package com.stedu.mall.orderservice.controller;

import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Goods;
import com.stedu.mall.common.bean.Order;
import com.stedu.mall.common.bean.OrderVo;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.OrderDetailService;
import com.stedu.mall.common.service.OrderService;

import com.stedu.mall.common.service.UserService;
import com.stedu.mall.common.utils.JwtUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;


    //添加 addrId cartIds[]
    @PostMapping
    public RespBean insert(@RequestBody OrderVo orderVo, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        orderVo.setUserId(userId);
        //添加订单
        orderService.insert(orderVo);
        return RespBean.ok("添加订单成功");
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
