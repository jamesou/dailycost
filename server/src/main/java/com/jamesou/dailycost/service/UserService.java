package com.jamesou.dailycost.service;

import com.jamesou.dailycost.bean.User;

public interface UserService {
    int register(User user);
    User getUser(int userId);
    int logoff(int userId);
}
