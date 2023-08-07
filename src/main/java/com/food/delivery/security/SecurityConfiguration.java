package com.food.delivery.security;

import com.food.delivery.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private static final String JWT_SIGNING_PASSWORD = "jkasdfjklasdfjkl";

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository)
    {
        return new UserAccountService(userAccountRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new MessageDigestPasswordEncoder("MD5");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter)
    {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public DefaultUserAuthenticationConverter defaultUserAuthenticationConverter(UserDetailsService userDetailsService)
    {
        DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
        defaultUserAuthenticationConverter.setUserDetailsService(userDetailsService);
        return defaultUserAuthenticationConverter;
    }

    @Bean
    public DefaultAccessTokenConverter defaultAccessTokenConverter(DefaultUserAuthenticationConverter defaultUserAuthenticationConverter)
    {
        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        defaultAccessTokenConverter.setUserTokenConverter(defaultUserAuthenticationConverter);
        return defaultAccessTokenConverter;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(DefaultAccessTokenConverter defaultAccessTokenConverter)
    {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(defaultAccessTokenConverter);
        converter.setSigningKey(JWT_SIGNING_PASSWORD);
        return converter;
    }
}
