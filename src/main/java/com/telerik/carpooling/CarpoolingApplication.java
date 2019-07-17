package com.telerik.carpooling;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@SpringBootApplication
@Log4j2
@EnableJpaAuditing
public class CarpoolingApplication {

    public static void main(String[] args) {
        log.error("test na loggera");
        SpringApplication.run(CarpoolingApplication.class, args);

    }

    @Component
    @RequiredArgsConstructor
    class Auditor implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            return Optional.of( authentication.getName());
        }
    }

}


