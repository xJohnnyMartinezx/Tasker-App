package com.codeup.tasker.cofig;


import com.codeup.tasker.models.User;
import com.codeup.tasker.services.UserDetailsLoader;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {


    public Optional<String> getCurrentAuditor(){
//        later can return the logged in user...after spring security has been implemnted.

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            } else {
               String loggedInUser =  SecurityContextHolder.getContext().getAuthentication().getName();

               return Optional.of(loggedInUser);
            }


//        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }
}
