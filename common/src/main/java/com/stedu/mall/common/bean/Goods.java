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
public class Goods implements Serializable {
    private Integer id;
    private String name; //名称
    private String dscp; //描述
    private String detail; //详情
    private BigDecimal price; //售价
    private BigDecimal markPrice; //标价
    private BigDecimal purchasePrice; //进价
    private String color; //颜色
    private String version; //版本
    private Integer count; //数量
    private Integer recom; //是否推荐 0:不推荐 1:推荐
    private Integer categoryId; //所属分类ID
    private Float score; //评分
    private Integer status; //是否上架 0:下架 1:上架
    private Category category; // 所属分类
    private List<GoodsPic> picList; //图片
}
