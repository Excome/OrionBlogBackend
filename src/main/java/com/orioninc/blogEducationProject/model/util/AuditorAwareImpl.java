package com.orioninc.blogEducationProject.model.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String loggedName = SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.ofNullable(loggedName);
    }

}
