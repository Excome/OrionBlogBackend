package com.orioninc.blogEducationProject.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication auth) throws ServletException, IOException
    {
        Map<String, String> responseBody = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        responseBody.put("auth", "SUCCESS");
        responseBody.put("username", auth.getName());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(responseBody));
    }
}
