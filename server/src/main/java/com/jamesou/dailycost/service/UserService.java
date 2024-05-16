package com.keepfool.bill.service;

import com.keepfool.bill.bean.User;

public interface UserService {
    int register(User user);
    User getUser(int userId);
    int logoff(int userId);
}
