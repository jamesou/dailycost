package com.jamesou.dailycost.controller;

import com.jamesou.dailycost.dao.UserDAO;
import com.jamesou.dailycost.dto.RegisterDTO;
import com.jamesou.dailycost.dto.ResultDTO;
import com.jamesou.dailycost.model.User;
import com.jamesou.dailycost.tools.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author jamesou
 */
@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;
    //@todo 用token来进行免登录验证, 在页面显示错误提示
    @GetMapping(value = {"/", "/index"})
    public String profile() {
        return "profile";
    }

    @PostMapping("/modify")
    @ResponseBody
    public Object modify(@RequestBody RegisterDTO registerDTO, HttpServletRequest request) {
        System.out.println(registerDTO);

        User user = new User();
        user.setName(registerDTO.getName());
        user.setId(registerDTO.getId());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        String token = TokenUtil.getTokenSecret(user.getName(), user.getPassword());
        user.setToken(token);
        int i = userDAO.updateById(user);
        ResultDTO resultDTO = ResultDTO.errorOf(i, "修改成功");
        request.getSession().setAttribute("user", user);
        return resultDTO;
    }
}
