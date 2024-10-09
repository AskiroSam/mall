package com.stedu.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collect implements Serializable {
    private Integer id; //主键
    private Integer goodsId; //商品id
    private Integer userId; //用户id
    private Date createTime; //收藏时间
    //收藏商品的详细信息
    private Goods goods;
}
