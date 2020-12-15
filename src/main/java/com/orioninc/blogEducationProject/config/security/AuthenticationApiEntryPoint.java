package com.orioninc.blogEducationProject.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orioninc.blogEducationProject.error.ApiError;
import com.orioninc.blogEducationProject.error.CmnError;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationApiEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = "Unauthorized user attempted to access the protected URL: " + request.getRequestURI();
        ApiError error = new ApiError(HttpStatus.FORBIDDEN, CmnError.ACCESS_DENIED, message, authException.getLocalizedMessage());
        ObjectMapper mapper = new ObjectMapper();
        String responseMsg = mapper.writeValueAsString(error);
        response.setContentType("application/json");
        response.getWriter().write(responseMsg);
    }
}
