package com.codeup.tasker.cofig;

import com.codeup.tasker.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    AuditorAware<User> auditorProvider(){
        return new AuditorAwareImpl();
    }

}
