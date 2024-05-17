package com.jamesou.dailycost.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamesou.dailycost.bean.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "User doesn't exist or password incorrect");
        map.put("loginState", false);
        httpServletResponse.getWriter().print(new ObjectMapper().writeValueAsString(map));
    }
}
