package ru.home.library.core.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Configuration
@ConfigurationProperties("spring.security")
@Validated
@Data
public class AuthProperties {

    @NotEmpty
    @Valid
    private List<UserProperty> users;

    @Data
    public static class UserProperty {

        private static final List<String> DEFAULT_ROLES = Collections.singletonList("USER");

        @NotEmpty
        private String name;
        @NotEmpty
        private String password;
        @NotEmpty
        private Set<String> roles = new HashSet<>(DEFAULT_ROLES);

    }

}