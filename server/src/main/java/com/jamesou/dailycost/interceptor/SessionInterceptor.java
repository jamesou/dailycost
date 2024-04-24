package com.jamesou.dailycost.interceptor;

import com.jamesou.dailycost.dao.UserDAO;
import com.jamesou.dailycost.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * session拦截器， 指定哪些需要拦截
 *
 * @author jamesou
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserDAO userDAO;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String[] ignore_uri = {"/login", "/register"};
        //获取请求的路径进行判断
        boolean flag = false;
        String servletPath = request.getServletPath();
        for (String s : ignore_uri) {
            if (servletPath.contains(s)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                //request.getRequestDispatcher("/login").forward(request, response);
                response.sendRedirect("/login");
            } else {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
