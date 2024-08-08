package com.fanclub.zinzin.global.config;

import com.fanclub.zinzin.global.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    public FilterConfig(TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Bean
    public FilterRegistrationBean<TokenAuthenticationFilter> loggingFilter() {
        FilterRegistrationBean<TokenAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenAuthenticationFilter);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}