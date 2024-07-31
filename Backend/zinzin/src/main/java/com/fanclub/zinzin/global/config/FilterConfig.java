package com.fanclub.zinzin.global.config;

import com.fanclub.zinzin.global.filter.TokenAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TokenAuthenticationFilter> loggingFilter() {
        FilterRegistrationBean<TokenAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new TokenAuthenticationFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
