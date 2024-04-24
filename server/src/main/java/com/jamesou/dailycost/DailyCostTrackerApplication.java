package com.jamesou.dailycost;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jamesou
 */
@SpringBootApplication
@MapperScan("com.jamesou.dailycost.dao")
public class DailyCostTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyCostTrackerApplication.class, args);
    }

}
