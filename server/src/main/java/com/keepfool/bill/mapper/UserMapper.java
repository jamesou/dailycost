package com.keepfool.bill.mapper;

import com.keepfool.bill.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    int userCount(User user);
    int register(User user);
    User getUser(int userId);
    int logoff(int userId);
    User getUserByAccount(String account);
}
