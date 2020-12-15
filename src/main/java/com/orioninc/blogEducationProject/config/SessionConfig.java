package com.orioninc.blogEducationProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 302400)
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {

}
