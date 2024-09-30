package com.stedu.mall.goodsservice.controller;

import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    //添加
    @PostMapping
    public RespBean insert(@RequestBody Category category) throws SteduException {
        categoryService.insert(category);
        return RespBean.ok("添加成功");
    }

    //根据id删除
    @DeleteMapping("/{id}")
    public RespBean delete(@PathVariable("id") Integer id) throws SteduException {
        categoryService.delete(id);
        return RespBean.ok("删除成功");
    }

    //根据id修改
    @PutMapping
    public RespBean update(@RequestBody Category category) throws SteduException {
        categoryService.update(category);
        return RespBean.ok("修改成功");
    }

    //修改是否推荐
    @PutMapping("/chgRecom")
    public RespBean chgRecom(@RequestBody Category category) throws SteduException {
        categoryService.update(category);
        return RespBean.ok("修改成功");
    }

    //修改是否上架
    @PutMapping("/chgStatus")
    public RespBean chgStatus(@RequestBody Category category) throws SteduException {
        categoryService.update(category);
        return RespBean.ok("修改成功");
    }

    //分页搜索
    @GetMapping("/search")
    public RespBean selectByPage(Category category, Integer pageNum, Integer pageSize) {
       //pageNum = pageNum == null ? 1 : pageNum;
       //pageSize = pageSize == null ? 5 : pageSize;

       Object data = categoryService.selectByCondition(category, pageNum, pageSize);
       return RespBean.ok("", data);
    }


    //根据id查询
    @GetMapping("/{id}")
    public RespBean selectById(@PathVariable("id") Integer id) {
        return RespBean.ok("", categoryService.selectBtyId(id));
    }

    //获取所有的夫分类
    @GetMapping("/allParent")
    public RespBean selectAllParent() {
        Category condition = new Category();
        condition.setParentId(0);

        Object data = categoryService.selectByCondition(condition, null, null);
        return RespBean.ok("", data);
    }
}
