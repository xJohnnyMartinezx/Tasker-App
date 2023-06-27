package com.codeup.tasker.cofig;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    public Optional<String> getCurrentAuditor(){
//        later can return the logged in user...after spring security has been implemnted.
        return Optinal.of();
    }
}
