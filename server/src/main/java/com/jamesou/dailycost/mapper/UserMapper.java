package com.jamesou.dailycost.mapper;

import com.jamesou.dailycost.bean.User;
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
