package com.jamesou.dailycost.dao;

import com.jamesou.dailycost.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author jamesou
 */
@Mapper
@Component
public interface UserDAO extends BaseMapper<User> {
}
