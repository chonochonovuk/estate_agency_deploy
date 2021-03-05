package com.ecoverde.estateagency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeanConfig {

    @Bean
    public PasswordEncoder bcrypt(){
        return new BCryptPasswordEncoder();
    }
}
