package com.stedu.mall.common.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    private String phone;
    private String email;
    private String realname;
    private Date createTime;
    private Integer status;
}

