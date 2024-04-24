package com.jamesou.dailycost.dto;

import lombok.Data;

/**
 * 返回登录注册后的结果
 *
 * @author jamesou
 */
@Data
public class ResultDTO {
    private Integer code;
    private String message;

    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }
}
