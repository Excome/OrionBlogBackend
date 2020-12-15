package com.orioninc.blogEducationProject.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orioninc.blogEducationProject.error.ApiError;
import com.orioninc.blogEducationProject.error.CmnError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger LOG = LogManager.getLogger(RestAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response , AccessDeniedException e) throws IOException, ServletException {
        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();
        String msg = "";
        if (auth != null) {
            msg = "User: " + auth.getName() + " attempted to access the protected URL: " + request.getRequestURI();
            LOG.warn(msg);
        }
        ApiError error = new ApiError(HttpStatus.FORBIDDEN, CmnError.ACCESS_DENIED, msg, e.getLocalizedMessage());
        ObjectMapper mapper = new ObjectMapper();
        String responseMsg = mapper.writeValueAsString(error);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(responseMsg);
    }
}
