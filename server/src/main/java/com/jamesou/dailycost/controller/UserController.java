package com.jamesou.dailycost.controller;

import com.jamesou.dailycost.bean.User;
import com.jamesou.dailycost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/user")
    public int register(@RequestBody User user) {
        return userService.register(user);
    }

    @ResponseBody
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUser(@PathVariable("userId") int userId) {
        Map<String, Object> map = new HashMap<>(16);
        User user = userService.getUser(userId);
        if (user == null) {
            map.put("message", "User doesn't exist");
        } else {
            map.put("userId", user.getUserId());
            map.put("userNickname", user.getUserNickname());
            map.put("userName", user.getUserName());
            map.put("userMail", user.getUserMail());
            map.put("userPhone", user.getUserPhone());
        }
        return map;
    }


    @ResponseBody
    @PutMapping("/user/{userId}")
    public int logoff(@PathVariable("userId") int userId) {
        return userService.logoff(userId);
    }
}
