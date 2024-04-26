package com.jamesou.dailycost.config;

import com.jamesou.dailycost.filter.SessionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * springmvc configuration
 *
 * @author jamesou
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    //add interceptor
}
