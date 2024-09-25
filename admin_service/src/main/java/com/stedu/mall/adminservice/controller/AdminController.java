package com.stedu.mall.adminservice.controller;

import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    //添加
    @PostMapping
    public RespBean insert(@RequestBody Admin admin) throws SteduException {
        adminService.insert(admin);
        return RespBean.ok("添加成功");
    }

    //根据id删除
    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id) throws SteduException {
        adminService.delete(id);
        return RespBean.ok("删除成功");
    }

    //修改可用状态
    @PutMapping("/id")
    public RespBean setStatus(@PathVariable("id") Integer id) throws SteduException {
        adminService.setStatus(id);
        return RespBean.ok("设置禁用成功");
    }

    //根据id修改
    @PutMapping
    public RespBean update(@RequestBody Admin admin) throws SteduException {
        adminService.update(admin);
        return RespBean.ok("修改成功");
    }

    //根据id查询
    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        return RespBean.ok("", adminService.selectById(id));
    }

    //分页搜索
    @GetMapping("/search")
    public RespBean selectByPage(Admin admin, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 5 : pageSize;

        Object data = adminService.selectByCondition(admin, pageNum, pageSize);
        return RespBean.ok("", data);
    }
}
