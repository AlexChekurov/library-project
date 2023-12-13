package ru.home.libraryservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import ru.home.library.core.config.AbstractSecurityConfig;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig extends AbstractSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
        entryPoint.setRealmName("library-service");

        http
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(STATELESS))
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(entryPoint))
                .httpBasic(httpBasicConfigurer -> httpBasicConfigurer.authenticationEntryPoint(entryPoint))
                .csrf(csrfConfigurer -> csrfConfigurer.disable());

        return http.build();
    }

}

