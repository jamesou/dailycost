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
    /** 用户姓名 */
    private String name;
    /** 用户邮箱 */
    private String email;
    /** 用户密码 */
    private String password;
    /** 用户token */
    private String token;
}
