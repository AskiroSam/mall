package com.stedu.mall.userservice.service;

import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.User;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.UserService;
import com.stedu.mall.userservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean insert(User user) {
        String password = SecureUtil.md5(SecureUtil.md5(user.getPassword()));
        user.setPassword(password);
        return userMapper.insert(user) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        return userMapper.delete(id) == 1;
    }

    @Override
    public boolean setStatus(Integer id, Integer status) {
        return userMapper.setStatus(id, status) == 1;
    }

    @Override
    public boolean update(User user) throws SteduException {
        User oldUser = userMapper.selectById(user.getId());
        if (user.getUsername() != null && user.getUsername().equals(oldUser.getUsername())) {
            throw new SteduException("用户名已经存在，无法添加");
        }
        return false;
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
}
