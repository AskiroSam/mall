package com.stedu.mall.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/*
* 表示商品分类的实体类
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    private Integer id;
    @NotBlank(message = "名称不能为空")
    private String name; //名称
    @NotBlank(message = "描述不能为空")
    private String dscp; //描述
    @NotBlank(message = "图片不能为空")
    private String pic; //图片
    @NotNull(message = "夫分类不能为空")
    @Min(value = 1, message = "夫分类不能为空")
    private Integer parentId; //夫分类的ID
    private Integer recom; //是否推荐 0:不推荐 1:推荐
    private Integer status; //是否上架 0:下架 1:上架

    private Category parent; //夫分类
    private List<Category> childList; //当前分类下所有的子分类
    private List<Goods> goodsList; //分类下的商品
}
