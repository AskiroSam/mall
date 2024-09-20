package com.stedu.mall.common.advide;


import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.exception.SteduException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

//全局异常处理
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(SteduException.class)
    public RespBean exceptionHandler(SteduException e) {
        e.printStackTrace();
        return RespBean.error(e.getMessage());
    }

    @ExceptionHandler({BindException.class})
    public RespBean handleMethodArgumentNotValidException(BindException e) {
        e.printStackTrace();
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sbf = new StringBuilder("校验失败 ");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            //sbf.append(fieldError.getField()).append("：").append(fieldError.getDefaultMessage()).append(", ");
            sbf.append("：").append(fieldError.getDefaultMessage()).append(", ");
        }
        String msg = sbf.toString();
        int i = msg.lastIndexOf(", ");
        msg = msg.substring(0, i);
        return RespBean.error(msg);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public RespBean handleConstraintViolationException(ConstraintViolationException e) {
        e.printStackTrace();
        return RespBean.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        e.printStackTrace();
        return RespBean.error("未知错误，请练习管理员");
    }
}
