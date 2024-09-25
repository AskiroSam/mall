package com.stedu.mall.adminservice.service;

import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.adminservice.mapper.AdminMapper;
import com.stedu.mall.common.bean.Admin;
import com.stedu.mall.common.bean.Category;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean insert(Admin admin) {
        admin.setStatus(1);
        String password = SecureUtil.md5(SecureUtil.md5(admin.getPassword()));
        admin.setPassword(password);
        return adminMapper.insert(admin) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        return adminMapper.delete(id) == 1;
    }

    @Override
    public boolean setStatus(Integer id) {
        return adminMapper.setStatus(id, 1) == 1;
    }

    @Override
    public boolean update(Admin admin) throws SteduException {
        Admin oldAdmin = adminMapper.selectById(admin.getId());
        if (admin.getUsername() != null && admin.getUsername().equals(oldAdmin.getUsername())) {
            throw new SteduException("用户名已经存在，无法添加");
        }
        return false;
    }

    @Override
    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    @Override
    public Object selectByCondition(Admin condition, Integer pageNum, Integer pageSize) {
        if (pageNum == null) { //不分页
            return adminMapper.selectByCondition(condition);
        } else { //分页
            //设置分页参数
            PageHelper.startPage(pageNum, pageSize);
            //查询
            List<Admin> admins = adminMapper.selectByCondition(condition);
            //创建分页信息的对象
            PageInfo<Admin> pageInfo = new PageInfo<>(admins);

            return pageInfo;
        }
    }
}
