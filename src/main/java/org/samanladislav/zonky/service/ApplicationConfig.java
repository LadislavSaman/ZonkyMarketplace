package org.samanladislav.zonky.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration of application.
 */
@Configuration
@ComponentScan("org.samanladislav.zonky")
public class ApplicationConfig {

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        return httpHeaders;
    }

}
