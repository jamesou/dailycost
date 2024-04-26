package com.jamesou.dailycost.service;

import com.jamesou.dailycost.dto.RegisterDTO;
import com.jamesou.dailycost.model.User;

/**
 * @author jamesou
 */
public interface UserService {


    User register(RegisterDTO registerDTO);
    User search(String email);


    User login(String email, String password);
}
