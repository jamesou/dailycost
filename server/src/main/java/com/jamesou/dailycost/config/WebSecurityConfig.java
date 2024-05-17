package com.jamesou.dailycost.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamesou.dailycost.bean.User;
import com.jamesou.dailycost.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserMapper userMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user").permitAll()
                .antMatchers("/**").authenticated()
                .and().formLogin().usernameParameter("account").passwordParameter("password").loginProcessingUrl("/login")
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailureHandler())
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("msg", "Logout Successfully");
                        map.put("logoutState", true);
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        httpServletResponse.getWriter().print(new ObjectMapper().writeValueAsString(map));
                    }
                })
                .and().csrf().disable()
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        httpServletResponse.setStatus(403);
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        Map<String, Object> map = new HashMap<>();
                        map.put("msg", "Can't be grant to access");
                        httpServletResponse.getWriter().print(new ObjectMapper().writeValueAsString(map));
                    }
                })
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        Map<String, Object> map = new HashMap<>();
                        map.put("msg", "User doesn't login in");
                        httpServletResponse.getWriter().print(new ObjectMapper().writeValueAsString(map));
                    }
                });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
                if (account == "" || account == null)
                    throw new RuntimeException("Username can't be empty");
                User user = userMapper.getUserByAccount(account);
                if (user == null)
                    throw new RuntimeException("User doesn't exist or password incorrect");
                return user;
            }
        }).passwordEncoder(new BCryptPasswordEncoder());
    }
}
