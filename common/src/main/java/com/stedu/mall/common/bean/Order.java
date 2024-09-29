package com.stedu.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private String id;
    private Integer userId;
    private Date createTime;
    private String express;
    private Integer payType;
    private Integer addrId;
    private String addrDetail;
    private Integer status;
    private User user;
    private List<OrderDetail> orderDetailList;
}
