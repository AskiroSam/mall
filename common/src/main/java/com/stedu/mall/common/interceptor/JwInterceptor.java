package com.stedu.mall.common.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stedu.mall.common.bean.RespBean;
import com.stedu.mall.common.bean.Rule;
import com.stedu.mall.common.config.WhiteListConfig;
import com.stedu.mall.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component
public class JwInterceptor implements HandlerInterceptor {
    @Autowired
    private WhiteListConfig whiteListConfig;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求路径
        String path = request.getRequestURI();
        //获取请求方式
        String method = request.getMethod();
        //// 获取白名单规则
        //List<Rule> rules = whiteListConfig.getRules();
        //// 规则为空
        //if (rules == null || rules.isEmpty()) {
        //    return false;
        //} else if(whiteListConfig.getRules()
        //        .stream()
        //        .anyMatch(rule -> rule.getMethod().equalsIgnoreCase(method) && path.matches(rule.getRegPath()))) {
        //    return true;
        //}
        //白名单 -- 判断请求是否在白名单中 -- 放行
        if (whiteListConfig.getRules() != null && whiteListConfig.getRules()
                .stream()
                .anyMatch(rule -> rule.getMethod().equalsIgnoreCase(method) && path.matches(rule.getRegPath()))) {
            return true;
        }

        /*
         * 预判浏览器发送过来的JWT是否正确
         * 1.正确 - 放行
         * 2.错误 - 不放行，返回错误信息
         * */
        //对OPTIONS请求放行，不然会出现跨域问题
        if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }
        //获取token
        String token = request.getHeader("token");
        RespBean respBean = null;
        try {
            //解析JWT，如果出现问题会抛出异常
            Map<String, Object> map = JwtUtils.parseJwtToMap(token);

            //生成新的JWT
            String jwt = JwtUtils.generateJwt(map);
            //将JWT放入response的响应头中
            response.setHeader("token", jwt);
            //暴露请求头token
            response.setHeader("Access-Control-Expose-Headers", "token");

            return true;
        } catch (SignatureVerificationException e) {
            respBean = RespBean.error("无效签名");
        } catch (TokenExpiredException e) {
            respBean = RespBean.error("令牌超时");
        } catch (AlgorithmMismatchException e) {
            respBean = RespBean.error("算法不匹配");
        } catch (Exception e) {
            respBean = RespBean.error("令牌无效");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(response.getOutputStream(), respBean);
        return false;
    }
}
