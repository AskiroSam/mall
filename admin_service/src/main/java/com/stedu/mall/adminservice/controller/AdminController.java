package com.stedu.mall.adminservice.controller;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.AdminService;
import com.stedu.mall.common.utils.JwtUtils;
import com.stedu.mall.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisUtil redisUtil;

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
    @PutMapping("/{id}")
    public RespBean setStatus(@PathVariable("id") Integer id) throws SteduException {
        adminService.setStatus(id);
        return RespBean.ok("设置禁用成功");
    }

    //根据id修改
    @PutMapping("/chgPwd")
    public RespBean update(String oldPwd, String newPwd, @RequestHeader("token") String token) throws SteduException {
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

    //生成验证码
    @GetMapping("/captcha")
    public RespBean captcha() {
        //创建验证码对象
        LineCaptcha captcha = new LineCaptcha(120, 38, 4, 10);
        //生成唯一的key -- 雪花算法生成
        String key = IdUtil.getSnowflakeNextIdStr();
        //将key和验证码的文本保存在Redis中 -- 超时时间60秒
        redisUtil.set(key, captcha.getCode(),60);
        //将验证码图片转换成Base64编码的形式
        String imageBase64Data = captcha.getImageBase64Data();
        //将key和Base64编码保存在map中
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("imageBase64Data", imageBase64Data);
        //将key和Base64的图片返回
        return RespBean.ok("生成验证码成功", map);
    }

    //登录
    @PostMapping("/login")
    public RespBean login(String username, String password, String key, String captchaInput) throws SteduException {
        //判断验证码是否正确 - 从Redis中查询验证码进行判断
        String captcha = (String)redisUtil.get(key);
        //登录
        if (captcha == null || !captcha.equalsIgnoreCase(captchaInput)) {
            throw new SteduException("验证码错误，请确认后重新登录");
        }
        Admin admin = adminService.login(username, password);

        //生成token
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", admin.getId());
        map.put("username", admin.getUsername());
        map.put("role", "admin");
        String jwtStr = JwtUtils.generateJwt(map);

        //将token返回给用户
        return RespBean.ok("登录成功", jwtStr);

    }

    @GetMapping("/info")
    public RespBean getLogin(@RequestHeader("token") String token) {
        //解析token
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        //获取用户id - 根据id查询用户的信息
        Integer id = (Integer) map.get("id");
        Admin admin = adminService.selectById(id);
        //返回
        return RespBean.ok("", admin);
    }


}
