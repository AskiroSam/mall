package com.stedu.mall.userservice.controller;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.bean.User;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.UserService;
import com.stedu.mall.common.utils.JwtUtils;
import com.stedu.mall.common.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

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

    //修改密码
    @PutMapping("/pwd")
    public RespBean updatePwd(@RequestBody User user) throws SteduException {
        userService.updatePwd(user);
        return RespBean.ok("修改成功");
    }

    //修改支付密码
    @PutMapping("/pay")
    public RespBean updatePay(@RequestBody User user) throws SteduException {
        userService.updatePay(user);
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

    //生成验证码
    @GetMapping("/captcha")
    public RespBean captcha() {
        //创建验证码对象
        LineCaptcha captcha = new LineCaptcha(120, 32, 5, 10);
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
            throw new SteduException("验证码错误，请确认后重新输入");
        }
        User user = userService.login(username, password);

        //生成token
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("role", "user");
        String jwtStr = JwtUtils.generateJwt(map);

        //将token返回给用户
        return RespBean.ok("登录成功", jwtStr);

    }

    //注册
    @PostMapping("/reg")
    public RespBean reg(String username, String password, String key, String captchaInput) throws SteduException {
        //判断验证码是否正确 - 从Redis中查询验证码进行判断
        String captcha = (String)redisUtil.get(key);
        //登录
        if (captcha == null || !captcha.equalsIgnoreCase(captchaInput)) {
            throw new SteduException("验证码错误，请确认后重新登录");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userService.reg(user);

        return RespBean.ok("注册成功");
    }

    //获取登录信息
    @GetMapping("/info")
    public RespBean getLogin(@RequestHeader("token") String token) {
        //解析token
        Map<String, Object> map = JwtUtils.parseJwtToMap(token);
        //获取用户id - 根据id查询用户的信息
        Integer id = (Integer) map.get("id");
        User user = userService.selectById(id);
        //返回
        return RespBean.ok("", user);
    }

}
