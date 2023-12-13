package ru.home.library.config;

import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Value("${library-service.client.user}")
    private String username;

    @Value("${library-service.client.password}")
    private String password;

    @Value("${library-service.client.retry.period}")
    private long period = 100;

    @Value("${library-service.client.retry.max-period}")
    private long maxPeriod = 2000;

    @Value("${library-service.client.retry.max-attempts}")
    private int maxAttempts = 3;

    @Bean
    public Retryer retryer() {

        return new Retryer.Default(
                period,
                maxPeriod,
                maxAttempts);
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(username, password);
    }
}
