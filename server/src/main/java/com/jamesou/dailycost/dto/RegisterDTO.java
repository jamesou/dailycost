package com.jamesou.dailycost.dto;

import lombok.Data;

/**
 * @author jamesou
 */
@Data
public class RegisterDTO {
    private Integer id;
    private String name;
    private String email;
    private String password;

}
