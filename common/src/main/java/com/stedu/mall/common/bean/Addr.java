package com.stedu.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Addr implements Serializable {
    private Integer id;
    //联系人
    private String contact;
    //手机号
    private String phone;
    //省
    private String province;
    //城市
    private String city;
    //区县
    private String district;
    //乡镇街道
    private String street;
    //详细地址
    private String address;
    //用户id
    private Integer userId;
    //状态 1:默认 0:非默认
    private Integer status;
}
