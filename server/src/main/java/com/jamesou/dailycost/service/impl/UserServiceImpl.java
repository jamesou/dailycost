package com.jamesou.dailycost.service.impl;

import com.jamesou.dailycost.dao.UserDAO;
import com.jamesou.dailycost.dto.RegisterDTO;
import com.jamesou.dailycost.model.User;
import com.jamesou.dailycost.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jamesou.dailycost.tools.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author jamesou
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;


    @Override
    public User register(RegisterDTO registerDTO) {
        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setName(registerDTO.getName());
        user.setPassword(registerDTO.getPassword());
        String token = TokenUtil.getTokenSecret(user.getName(), user.getPassword());
        user.setToken(token);
        userDAO.insert(user);
        return user;
    }

    @Override
    public User search(String email) {
        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>().lambda();
        queryWrapper.eq(User::getEmail, email);
        User user = userDAO.selectOne(queryWrapper);
        return user;
    }

    @Override
    public User login(String email, String password) {
        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>().lambda();
        queryWrapper.eq(User::getEmail, email)
                .eq(User::getPassword, password);
        User user = userDAO.selectOne(queryWrapper);

        return user;
    }
}
