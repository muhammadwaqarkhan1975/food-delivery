package com.food.delivery.configuration;

import com.food.delivery.security.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        SecurityUser user = getUser();
        return user != null ? Optional.ofNullable(user.getUserId()) : Optional.empty();
    }

    private SecurityUser getUser()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null)
            return null;
        else
            return principal instanceof SecurityUser ? ((SecurityUser) principal) : null;
    }
}
