package com.jamesou.dailycost.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author jamesou
 */
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String email;

    private String password;

    private String token;
}
