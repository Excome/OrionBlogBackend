package com.orioninc.blogEducationProject.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        Map<String, String> responseBody = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        responseBody.put("auth", "LOGOUT");
        if(auth != null) {
            responseBody.put("username", auth.getName());
        }else {
            responseBody.put("username", null);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(responseBody));
    }
}
