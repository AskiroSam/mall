package com.stedu.mall.common.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String payPassword;
    @JsonIgnore
    private String salt;
    private String realname;
    private String sex;
    private String idCard;
    private String phone;
    private String email;
    private Date regTime;
    private BigDecimal money;
    private Integer status;
}
