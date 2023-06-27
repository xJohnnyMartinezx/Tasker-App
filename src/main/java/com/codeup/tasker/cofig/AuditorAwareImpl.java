package com.codeup.tasker.cofig;

import com.codeup.tasker.models.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor(){
//        later can return the logged in user...after spring security has been implemnted.
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.of(loggedInUser);
    }
}
