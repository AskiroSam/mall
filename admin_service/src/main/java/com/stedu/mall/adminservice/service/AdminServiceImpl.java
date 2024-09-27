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
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean insert(Admin admin) {
        //初始化状态
        admin.setStatus(1);
        //设置盐
        String string = UUID.randomUUID().toString();
        String last8Chars = string.substring(string.length() - 8);
        admin.setSalt(last8Chars);
        //密码MD5和盐加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(admin.getPassword() + admin.getSalt()));
        admin.setPassword(md5Pwd);
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
    public boolean chgPwd(String oldPwd, String newPwd, Integer id) throws SteduException {
        /*
        * 判断原密码是否正确
        *   1.根据id查询用户信息 - 获取原密码和盐
        *   2.使用用户输入的新密码和盐加密
        *   3.和数据库中的原密码比较
        * */
        //根据id查询用户信息 - 获取原密码和盐
        Admin admin = adminMapper.selectById(id);
        String salt = admin.getSalt();
        //使用用户输入的新密码和盐加密 -> 加密之后的用户输入的密码
        String md5InputOldPwd = SecureUtil.md5(SecureUtil.md5(oldPwd + salt));
        if (!admin.getPassword().equals(md5InputOldPwd)) {
            throw new SteduException("原密码输入错误，请确认后重新输入");
        }
        //对新密码结合盐进行加密
        String md5NewPwd = SecureUtil.md5(SecureUtil.md5(newPwd + salt));

        //修改密码
        admin = new Admin();
        admin.setId(id);
        admin.setPassword(md5NewPwd);

        return adminMapper.update(admin) == 1;
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

    @Override
    public Admin login(String username, String password) throws SteduException {
        //根据用户名进行查询
        Admin admin = adminMapper.selectByUserName(username);
        if (admin == null) {
            throw new SteduException("用户名错误");
        }
        //对用户输入的密码进行加密 -- 使用MD5算法和盐进行加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(password + admin.getSalt()));
        //对加密之后的密码和数据库中的密码进行比较
        if (!md5Pwd.equals(admin.getPassword())) {
            throw new SteduException("密码错误");
        }

        return admin;
    }
}
