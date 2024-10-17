package com.stedu.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods implements Serializable {
    private Integer id;
    @NotBlank(message = "名称不能为空")
    private String name; //名称
    @NotBlank(message = "描述不能为空")
    private String dscp; //描述
    private String detail; //详情
    @NotNull(message = "售价不能为空")
    private BigDecimal price; //售价
    @NotNull(message = "标价不能为空")
    private BigDecimal markPrice; //标价
    @NotNull(message = "进价不能为空")
    private BigDecimal purchasePrice; //进价
    @NotBlank(message = "颜色不能为空")
    private String color; //颜色
    @NotBlank(message = "版本不能为空")
    private String version; //版本
    @NotNull(message = "数量不能为空")
    private Integer count; //数量
    private Integer recom; //是否推荐 0:不推荐 1:推荐
    @NotNull(message = "分类不能为空")
    @Min(value = 1, message = "分类不能为空")
    private Integer categoryId; //所属分类ID
    private Float score; //评分
    private Integer status; //是否上架 0:下架 1:上架
    private Category category; // 所属分类
    @NotNull(message = "图片不能为空")
    private List<GoodsPic> picList; //图片
}
