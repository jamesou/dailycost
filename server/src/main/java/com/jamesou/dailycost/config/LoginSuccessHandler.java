package com.keepfool.bill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepfool.bill.bean.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Authentication authentication) throws IOException, ServletException {

        httpServletResponse.setContentType("application/json;charset=utf-8");
        User user = (User) authentication.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "Login Successfully");
        map.put("loginState", true);
        map.put("userId", user.getUserId());
        map.put("userNickname", user.getUserNickname());
        map.put("userName", user.getUserName());
        map.put("userMail", user.getUserMail());
        map.put("userPhone", user.getUserPhone());
        httpServletResponse.getWriter().print(new ObjectMapper().writeValueAsString(map));
        // new ObjectMapper().writeValueAsString()  以json格式输出
    }
}
