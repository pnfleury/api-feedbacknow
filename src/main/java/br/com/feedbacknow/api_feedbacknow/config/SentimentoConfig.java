package br.com.feedbacknow.api_feedbacknow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SentimentoConfig {

    // Cria um Bean para o RestTemplate, que será injetado nos serviços.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}