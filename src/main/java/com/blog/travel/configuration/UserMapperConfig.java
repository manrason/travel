package com.blog.travel.configuration;

import com.blog.travel.mapper.UserMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.blog.travel.mapper.UserMapper;

@Configuration
public class UserMapperConfig {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }
}