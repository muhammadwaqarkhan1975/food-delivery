package com.food.delivery.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
    private final AuthenticationManager authenticationManager;
    private final TokenStore tokenStore;
    private final JwtAccessTokenConverter accessTokenConverter;

    private static final String APPLICATION_CLIENT_ID = "FoodDelivery";
    private static final String CLIENT_SECRET_KEY_ENCODED = "5f4dcc3b5aa765d61d8327deb882cf99";
    private static final String GRANT_TYPE_PASSWORD = "password";

    @Autowired
    public OAuth2AuthorizationServerConfiguration(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager, TokenStore tokenStore, JwtAccessTokenConverter accessTokenConverter)
    {
        this.authenticationManager = authenticationManager;
        this.tokenStore = tokenStore;
        this.accessTokenConverter = accessTokenConverter;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        security.addTokenEndpointAuthenticationFilter(new CorsFilter(source));
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception
    {
        clients.inMemory()
                .withClient(APPLICATION_CLIENT_ID)
                .secret(CLIENT_SECRET_KEY_ENCODED)
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD)
                .autoApprove(true)
                .scopes("read", "write", "trust");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
    {
        endpoints
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .authenticationManager(authenticationManager).allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.OPTIONS);
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices()
    {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }
}
