package com.jamesou.dailycost.service;

import com.jamesou.dailycost.dto.RegisterDTO;
import com.jamesou.dailycost.model.User;

/**
 * @author jamesou
 */
public interface UserService {



    /**
     * 根据输出的用户注册
     * @param registerDTO
     * @return
     */
    User register(RegisterDTO registerDTO);

    /**
     * 登录
     * @param email  用户的邮箱
     * @param password  用户的密码
     * @return  结果
     */
    User login(String email, String password);
}
