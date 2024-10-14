package com.stedu.mall.userservice.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.bean.User;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.UserService;
import com.stedu.mall.userservice.mapper.AddrMapper;
import com.stedu.mall.userservice.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
//Dubbo注册服务
@DubboService
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddrMapper addrMapper;

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

    //登录
    @Override
    public User login(String username, String password) throws SteduException {
        //根据用户名查询判断用户是否存在
        User user = userMapper.selectByUserName(username);
        if (user == null) {
            throw new SteduException("该用户不存在，无法登录");
        }

        //判断用户是否禁用或注销
        if (user.getStatus().equals(2) || user.getStatus().equals(3)) {
            throw new SteduException("该用户被禁用或注销，无法登录");
        }

        //对输入的密码进行加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(password + user.getSalt()));

        //和数据库中的密码对比
        if (!md5Pwd.equals(user.getPassword())) {
            throw new SteduException("密码输入错误。无法登录");
        }

        return user;
    }

    //注册
    @Override
    public boolean reg(User user) throws SteduException {
        //判断该用户名在系统中是否存在
        if (userMapper.selectByUserName(user.getUsername()) != null) {
            throw new SteduException("该用户已经存在，无法注册");
        }

        //生成盐
        String salt = RandomUtil.randomString(8);
        user.setSalt(salt);

        //对密码进行加密
        String md5Pwd = SecureUtil.md5(SecureUtil.md5(user.getPassword() + salt));
        user.setPassword(md5Pwd);
        //同时设置支付密码 默认是 “123456”
        String md5Pay = SecureUtil.md5(SecureUtil.md5("123456" + salt));
        user.setPayPassword(md5Pay);

        user.setStatus(0);
        user.setIsOldPwdVerified(0);



        //添加用户
        return userMapper.insert1(user) == 1;

    }

    @Override
    public boolean delete(Integer id) throws SteduException {
        //先注销再删除
        User user = userMapper.selectById(id);
        if (user.getStatus() != 3) {
            throw new SteduException("请先注销后再删除");
        }
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

        String oldPswd = oldUser.getPassword();
        if (user.getPassword().equals(oldPswd)) {
            user.setPassword(oldPswd);
        } else {
            String newPswd = SecureUtil.md5(SecureUtil.md5(user.getPassword() + oldUser.getSalt()));
            user.setPassword(newPswd);
        }
        return userMapper.update(user) == 1;
    }

    @Override
    public boolean updatePwd(User user) throws SteduException {

        User oldUser = userMapper.selectById(user.getId());
        String oldPswd = oldUser.getPassword();
        String newPswd = SecureUtil.md5(SecureUtil.md5(user.getPassword() + oldUser.getSalt()));


        if (newPswd != null && oldUser.getIsOldPwdVerified() == 0) {
            if (!newPswd.equals(oldPswd)) {
                user.setPassword(oldPswd);
                throw new SteduException("原密码错误，请重新输入");
            } else {
                user.setPassword(oldPswd);
                user.setIsOldPwdVerified(1);
            }
        } else if (oldUser.getIsOldPwdVerified() == 1) {
            user.setPassword(newPswd);
            user.setIsOldPwdVerified(0);
        }

        return userMapper.updatePwd(user) == 1;
    }

    @Override
    public boolean updatePay(User user) throws SteduException {

        User oldUser = userMapper.selectById(user.getId());
        String oldPsay = oldUser.getPayPassword();
        String newPsay = SecureUtil.md5(SecureUtil.md5(user.getPayPassword() + oldUser.getSalt()));



        if (newPsay != null && oldUser.getIsOldPwdVerified() == 0) {
            if (!newPsay.equals(oldPsay)) {
                user.setPayPassword(oldPsay);
                throw new SteduException("原密码错误，请重新输入");
            } else {
                user.setPayPassword(oldPsay);
                user.setIsOldPwdVerified(1);
            }
        } else if (oldUser.getIsOldPwdVerified() == 1) {
            user.setPayPassword(newPsay);
            user.setIsOldPwdVerified(0);
        }

        return userMapper.updatePay(user) == 1;
    }

    @Override
    public boolean updateInMoney(User user) throws SteduException {
        User oldUser = userMapper.selectById(user.getId());

        //如果找不到用户，抛出异常
        if (oldUser == null) {
            throw new SteduException("用户不存在");
        }

        //获取金额
        BigDecimal oldMoney = oldUser.getMoney();
        BigDecimal newMoney = user.getMoney();

        //验证新金额，确保它不为负数
        if (newMoney == null || newMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new SteduException("新的金额无效，不能为负数");
        }

        BigDecimal totalMoney = oldMoney.add(newMoney);
        oldUser.setMoney(totalMoney);

        return userMapper.updateMoney(oldUser) == 1;
    }

    @Override
    public boolean updateOutMoney(User user) throws SteduException {
        User oldUser = userMapper.selectById(user.getId());

        //如果找不到用户，抛出异常
        if (oldUser == null) {
            throw new SteduException("用户不存在");
        }

        //获取金额
        BigDecimal oldMoney = oldUser.getMoney();
        BigDecimal newMoney = user.getMoney();

        //验证新金额，确保它不为负数
        if (newMoney == null || newMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new SteduException("新的金额无效，不能为负数");
        }

        BigDecimal totalMoney = oldMoney.subtract(newMoney);
        oldUser.setMoney(totalMoney);
        if (totalMoney.compareTo(BigDecimal.ZERO) < 0) {
            throw new SteduException("更新后的金额不能为负数");
        }

        return userMapper.updateMoney(oldUser) == 1;
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

    @Override
    public Addr selectByAddrId(Integer id) {
        return addrMapper.selectById(id);
    }

    @Override
    public boolean payUpdate(User user) {
        return userMapper.payUpdate(user) == 1;
    }
}
