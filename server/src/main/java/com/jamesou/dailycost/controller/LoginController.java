package com.jamesou.dailycost.controller;

import com.jamesou.dailycost.dto.RegisterDTO;
import com.jamesou.dailycost.dto.ResultDTO;
import com.jamesou.dailycost.model.User;
import com.jamesou.dailycost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jamesou
 */
@Controller
public class LoginController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 处理登录页面传来的json，
     * 如果用户不存在，则返回错误
     * 如果用户存在， 返回表示登录成功的json， 并把用户信息存入 session
     *
     * @param map
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Object login(@RequestBody Map<String, String> map, HttpServletResponse response,
                        HttpServletRequest request) {
        String email = map.get("email");
        String password = map.get("password");
        System.out.println(map);
        User user = userService.login(email, password);
        ResultDTO resultDTO = null;
        if (user == null) {
            resultDTO = ResultDTO.errorOf(0, "用户密码错误");
        } else {
            Cookie cookie = new Cookie("token", user.getToken());
            response.addCookie(cookie);
            resultDTO = ResultDTO.errorOf(1, "");
            //如果登录，则把用户存入session
            request.getSession().setAttribute("user", user);
        }

        return resultDTO;
    }

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    /**
     * 处理注册页面传来的json信息, 以json返回
     *
     * @param registerDTO
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public Object register(@RequestBody RegisterDTO registerDTO,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        System.out.println(registerDTO);
        User user = userService.register(registerDTO);
        ResultDTO resultDTO;
        if (user == null) {
            resultDTO = ResultDTO.errorOf(0, "email 已注册");
        } else {
            Cookie cookie = new Cookie("token", user.getToken());
            response.addCookie(cookie);
            resultDTO = ResultDTO.errorOf(1, "");
            //如果注册，则把用户存入session
            request.getSession().setAttribute("user", user);
        }
        return resultDTO;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return "/login";
    }

}
