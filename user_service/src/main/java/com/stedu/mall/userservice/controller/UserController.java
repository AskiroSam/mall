package com.stedu.mall.userservice.controller;

import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.bean.User;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    //添加
    @PostMapping
    public RespBean insert(@RequestBody User user) throws SteduException {
        userService.insert(user);
        return RespBean.ok("添加成功");
    }

    //根据id删除
    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id) throws SteduException {
        userService.delete(id);
        return RespBean.ok("删除成功");
    }

    //修改状态
    @PutMapping("/{status}")
    public RespBean setStatus(Integer status) throws SteduException {
        userService.setStatus(status);
        return RespBean.ok("修改状态成功");
    }

    //根据id修改
    @PutMapping
    public RespBean update(@RequestBody User user) throws SteduException {
        userService.update(user);
        return RespBean.ok("修改成功");
    }

    //根据id查询
    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        return RespBean.ok("", userService.selectById(id));
    }

    //分页搜索
    @GetMapping("/search")
    public RespBean selectByPage(User user, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        Object data = userService.selectByCondition(user, pageNum, pageSize);
        return RespBean.ok("", data);
    }
}
