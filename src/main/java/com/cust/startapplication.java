package com.cust;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.rmi.server.ExportException;

@SpringBootApplication
@MapperScan("com.cust.dao")
public class startapplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ExportException.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
