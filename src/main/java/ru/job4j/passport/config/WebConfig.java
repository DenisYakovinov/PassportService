package ru.job4j.passport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {

    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }
}
