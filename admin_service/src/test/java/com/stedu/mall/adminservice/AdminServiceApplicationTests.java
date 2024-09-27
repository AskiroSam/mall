package com.stedu.mall.adminservice;

import cn.hutool.crypto.SecureUtil;
import com.stedu.mall.adminservice.mapper.AdminMapper;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.AdminService;
import com.stedu.mall.common.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class AdminServiceApplicationTests {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminService adminService;

    @Test
    public void test11() throws SteduException {
        adminService.chgPwd("12345", "123", 1);
    }

    @Test
    public void test01() {
        redisUtil.set("name", "zhangsan");
    }

    @Test
    public void test02() {
        System.out.println(adminMapper.selectByUserName("tom"));
    }

    @Test
    public void test03() {
        String md5Pwd = SecureUtil.md5(SecureUtil.md5("123" + "981a735b"));
        System.out.println(md5Pwd);
    }

    @Test
    public void test04() throws SteduException {
        System.out.println(adminService.login("admin", "12334"));
    }

    @Test
    public void test05()  {
        String string = UUID.randomUUID().toString();
        String last8Chars = string.substring(string.length() - 8);
        System.out.println(last8Chars);
    }


}
