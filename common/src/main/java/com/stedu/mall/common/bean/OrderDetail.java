package com.stedu.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {
    private Integer id; //主键
    private Integer count;
    private BigDecimal price;
    private String orderId;
    private Integer goodsId;
    private Goods goods;
    private List<Goods> goodsList;
}
