package ru.home.library.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.home.library.core.properties.AuthProperties;

@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true
)
@Import({AuthProperties.class})
public abstract class AbstractSecurityConfig {
    protected static final String[] PERMITTED_ENDPOINTS = new String[]{
            "health", "info", "prometheus", "integrationgraph", "beans", "conditions", "configprops", "env",
            "mappings", "scheduledtasks", "loggers", "threaddump"};

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring().requestMatchers(
                    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger-ui.html",
                    "/webjars/**");
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService(AuthProperties authProperties) {
        var manager = new InMemoryUserDetailsManager();
        authProperties.getUsers().stream()
                .map(this::mapToUserDetails)
                .forEach(manager::createUser);
        return manager;
    }

    private UserDetails mapToUserDetails(AuthProperties.UserProperty userProperty) {
        return User.withUsername(userProperty.getName())
                .password(userProperty.getPassword())
                .roles(userProperty.getRoles().toArray(new String[0]))
                .build();
    }
}
