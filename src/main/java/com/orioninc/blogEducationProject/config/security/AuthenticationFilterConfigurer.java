package com.orioninc.blogEducationProject.config.security;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class AuthenticationFilterConfigurer<H extends HttpSecurityBuilder<H>> extends
        AbstractAuthenticationFilterConfigurer<H, AuthenticationFilterConfigurer<H>, AuthenticationFilter> {

    public AuthenticationFilterConfigurer(AuthenticationFilter authenticationFilter, String defaultLoginProcessingUrl) {
        super(authenticationFilter, defaultLoginProcessingUrl);
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    @Override
    public AuthenticationFilterConfigurer<H> loginProcessingUrl(String loginProcessingUrl) {
        return super.loginProcessingUrl(loginProcessingUrl);
    }


}
