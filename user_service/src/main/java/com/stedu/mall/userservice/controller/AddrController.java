package com.stedu.mall.userservice.controller;

import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.AddrService;
import com.stedu.mall.common.utils.JwtUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/addr")
public class AddrController {
    @Autowired
    private AddrService addrService;

    @PostMapping
    //添加地址
    public RespBean insert(@RequestBody Addr addr, @RequestHeader("token") String token) {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        addr.setUserId(userId);
        addrService.insert(addr);

        return RespBean.ok("添加成功");
    }

    @DeleteMapping("/{id}")
    //删除地址
    public RespBean delete(@PathVariable("id") Integer id, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        addrService.delete(id, userId);
        return RespBean.ok("删除成功");
    }

    @PutMapping
    //修改地址
    public RespBean update(@RequestBody Addr addr, @RequestHeader("token") String token) throws SteduException {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        addrService.update(addr, userId);

        return RespBean.ok("修改成功");
    }

    @GetMapping()
    //查询当前用户的所有地址
    public RespBean selectByCondition(@RequestHeader("token") String token) {
        //解析token获取用户id
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        Integer userId = (Integer)map.get("id");

        Addr condition = new Addr();
        condition.setUserId(userId);

        List<Addr> addrList = addrService.selectByCondition(condition);

        return RespBean.ok("查询成功", addrList);
    }

    @GetMapping("/{id}")
    //根据id查询
    public RespBean selectById(@PathVariable("id") Integer id) {

        Addr addr = addrService.selectById(id);

        return RespBean.ok("查询成功", addr);
    }

}
