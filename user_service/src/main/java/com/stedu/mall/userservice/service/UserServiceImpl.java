package com.stedu.mall.userservice.service;

import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.User;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.UserService;
import com.stedu.mall.userservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
//Dubbo注册服务
@DubboService
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean insert(User user) throws SteduException {
        //设置盐
        String string = UUID.randomUUID().toString();
        String last8Chars = string.substring(string.length() - 8);
        user.setSalt(last8Chars);
        //密码MD5和盐加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(user.getPassword() + user.getSalt()));
        user.setPassword(md5Pwd);
        //身份证号不能重复
        for (User checkUser : userMapper.selectAll()) {
            if (user.getIdCard().equals(checkUser.getIdCard())) {
                throw new SteduException("身份证号已存在，无法添加");
            }
        }
        return userMapper.insert(user) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        return userMapper.delete(id) == 1;
    }

    @Override
    public boolean setStatus(Integer status) {
        return userMapper.setStatus(status) == 1;
    }

    @Override
    public boolean update(User user) throws SteduException {
        User oldUser = userMapper.selectById(user.getId());
        // 检查身份证号码是否已存在且与当前用户不同
        if (user.getIdCard() != null && !user.getIdCard().equals(oldUser.getIdCard())) {
            for (User checkUser : userMapper.selectAll()) {
                if (user.getIdCard().equals(checkUser.getIdCard())) {
                    throw new SteduException("身份证号码已存在，无法修改");
                }
            }
        }
        return userMapper.update(user) == 1;
    }

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }


    @Override
    public Object selectByCondition(User condition, Integer pageNum, Integer pageSize) {
        if (pageNum == null) { //不分页
            return userMapper.selectByCondition(condition);
        } else { //分页
            //设置分页参数
            PageHelper.startPage(pageNum, pageSize);
            //查询
            List<User> users = userMapper.selectByCondition(condition);
            //创建分页信息的对象
            PageInfo<User> pageInfo = new PageInfo<>(users);

            return pageInfo;
        }
    }

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }
}
