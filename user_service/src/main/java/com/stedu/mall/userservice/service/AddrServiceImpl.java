package com.stedu.mall.userservice.service;

import com.stedu.mall.common.bean.Addr;
import com.stedu.mall.common.exception.SteduException;
import com.stedu.mall.common.service.AddrService;
import com.stedu.mall.userservice.mapper.AddrMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService
public class AddrServiceImpl implements AddrService {
    @Autowired
    private AddrMapper addrMapper;

    @Override
    public int insert(Addr addr) {
        addr.setStatus(0);
        return addrMapper.insert(addr);
    }

    @Override
    public int delete(Integer id, Integer userId) throws SteduException {
        //判断这个地址是否存在
        Addr addr = addrMapper.selectById(id);
        if (addr == null) {
            throw new SteduException("该地址不存在，无法删除");
        }

        //判断这个地址是否属于当前用户
        if (!addr.getUserId().equals(userId)) {
            throw new SteduException("该地址属于其它用户，无法删除");
        }

        //删除
        return addrMapper.delete(id);
    }

    @Override
    public int update(Addr addr, Integer userId) throws SteduException {
        //判断这个地址是否存在
        Addr a = addrMapper.selectById(addr.getId());
        if (a == null) {
            throw new SteduException("该地址不存在，无法删除");
        }

        //判断这个地址是否属于当前用户
        if (!a.getUserId().equals(userId)) {
            throw new SteduException("该地址属于其它用户，无法删除");
        }

        /*
        * 设置为默认地址的处理 -- 如果将某个地址设置为默认地址
        * 1.将之前的默认地址设置为普通的地址
        * */
        if (Integer.valueOf(1).equals(addr.getStatus())) {
            //查出当前的默认地址
            Addr condition = new Addr();
            condition.setStatus(1);
            List<Addr> addrList = addrMapper.selectByCondition(condition);

            //将当前的默认地址设置为普通地址
            if (addrList.size() > 0) {
                Addr a1 = new Addr();
                a1.setId(addrList.get(0).getId());
                a1.setStatus(0);

                addrMapper.update(a1);
            }
        }

        return addrMapper.update(addr);
    }

    @Override
    public Addr selectById(Integer id) {
        return addrMapper.selectById(id);
    }

    @Override
    public List<Addr> selectByCondition(Addr condition) {
        return addrMapper.selectByCondition(condition);
    }
}
