package com.jamesou.dailycost.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author jamesou
 * @todo MD5 encrypt
 */
@Controller
public class LoginController {
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @GetMapping(value={"/","/login"})
    public String login() {
        return "login";
    }

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
            resultDTO = ResultDTO.errorOf(0, "Email Or Password incorrect");
        } else {
            Cookie cookie = new Cookie("token", user.getToken());
            response.addCookie(cookie);
            resultDTO = ResultDTO.errorOf(1, "Sign in successfully");
            request.getSession().setAttribute("user", user);
        }

        return resultDTO;
    }


    @GetMapping("/register_page")
    public String register() {
        return "register";
    }


    @PostMapping("/register")
    @ResponseBody
    public Object register(@RequestBody RegisterDTO registerDTO,
                           HttpServletResponse response,
                           HttpServletRequest request) {
        User user = userService.search(registerDTO.getEmail());
        ResultDTO resultDTO;
        if (user != null) {
            resultDTO = ResultDTO.errorOf(0, "User existed already");
        } else {
            userService.register(registerDTO);
            resultDTO = ResultDTO.errorOf(1, "User register successfully, Please sign in!");
        }
        return resultDTO;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0); //set it invalid
        return "/login";
    }

}
