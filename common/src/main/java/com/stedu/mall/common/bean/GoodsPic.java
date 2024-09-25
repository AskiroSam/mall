package com.stedu.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPic implements Serializable {
    private Integer id; //主键
    private String url; //图片名字
    private Integer goodsId; //商品ID - 商品主键
}
