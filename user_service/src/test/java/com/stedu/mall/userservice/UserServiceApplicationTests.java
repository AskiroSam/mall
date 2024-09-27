package com.stedu.mall.userservice;

import com.stedu.mall.common.service.UserService;
import com.stedu.mall.userservice.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01() {
        System.out.println(userMapper.selectById(1));
    }
}
