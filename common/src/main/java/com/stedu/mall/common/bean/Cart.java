package com.stedu.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart implements Serializable {
    private Integer id;
    private Integer count;
    private Integer userId;
    private Integer goodsId;
    //购物车对应的商品
    private Goods goods;
    //购物车中多个商品
    private List<Integer> idList;
}
