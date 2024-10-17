package com.stedu.mall.common.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements Serializable {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    @NotBlank(message = "手机号不能为空")
    private String phone;
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "真实姓名不能为空")
    private String realname;
    private Date createTime;
    private Integer status;
}

