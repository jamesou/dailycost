package com.keepfool.bill.controller;

import com.keepfool.bill.bean.User;
import com.keepfool.bill.service.UserService;
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

    /**
     * 注册账号
     * @param user
     * @return 用户id，-1：用户有重复
     */
    @ResponseBody
    @PostMapping("/user")
    public int register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 获取指定id用户
     * @param userId
     * @return user对象，null：用户已注销
     */
    @ResponseBody
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUser(@PathVariable("userId") int userId) {
        Map<String, Object> map = new HashMap<>(16);
        User user = userService.getUser(userId);
        if (user == null) {
            map.put("message", "用户已注销");
        } else {
            map.put("userId", user.getUserId());
            map.put("userNickname", user.getUserNickname());
            map.put("userName", user.getUserName());
            map.put("userMail", user.getUserMail());
            map.put("userPhone", user.getUserPhone());
        }
        return map;
    }

    /**
     * 注销
     * @param userId
     * @return 1：注销成功，0：注销失败
     */
    @ResponseBody
    @PutMapping("/user/{userId}")
    public int logoff(@PathVariable("userId") int userId) {
        return userService.logoff(userId);
    }
}
