package com.food.delivery.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{
    @Override
    public void configure(HttpSecurity config) throws Exception
    {
        config
                .authorizeRequests()
                .antMatchers("/oauth/token", "/graphiql/**","/swagger-ui/**","/api-docs/**", "/v2/api-docs").permitAll()
                .anyRequest().authenticated().and()
                .cors().and()
                .csrf().disable();
    }
}
