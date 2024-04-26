package com.jamesou.dailycost.filter;
import com.jamesou.dailycost.model.User;
import com.jamesou.dailycost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author jamesou
 * @todo check token
 */
@Component
@Order(1)
public class SessionFilter extends OncePerRequestFilter {
    @Autowired
    UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        if (isExcluded(requestUri) || isValidSession(request)) {
            filterChain.doFilter(request, response);
        } else {
//          System.out.println("requestUri:"+requestUri+",else errorMsg:"+errorMsg);
            String errorMsg = (String) request.getAttribute("errorMsg");
            request.getSession().setAttribute("errorMsg",errorMsg);
            response.sendRedirect("/login");
        }
    }

    private boolean isValidSession(HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        String ERRPR_MSG = "errorMsg";
        if (sessionUser == null) {
            request.setAttribute(ERRPR_MSG, "Unauthorized access. Please login first.");
            return false;
        } else {
            User user = userService.search(sessionUser.getEmail());
            if(user==null){
                // Set error message
                request.setAttribute(ERRPR_MSG, "User don't exist, please sign up first.");
                return false;
            }else{
                request.getSession().setAttribute("user",user);
                return true;
            }
        }
    }

    private boolean isExcluded(String requestUri) {
        return  requestUri.matches(".*\\.(ttf|woff2|woff|ico|css|js|jpg|png|gif)$") ||
                requestUri.equals("/")||
                requestUri.equals("/login")||
                requestUri.equals("/register_page")||
                requestUri.equals("/register");
    }
}
