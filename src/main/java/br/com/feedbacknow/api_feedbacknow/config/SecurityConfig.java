package br.com.feedbacknow.api_feedbacknow.config;

import jakarta.servlet.http.HttpServletResponse; // Import necessário para o Response
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //TRATADOR de erro de login
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"erro\": \"Não autorizado\", \"detalhe\": \"Usuário ou senha inválidos ou ausentes.\"}");
        };
    }

    //Definimos a Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // APIs REST não precisam de CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated() // Bloqueia tudo por padrão
                )
                // Aqui dizemos ao Spring para usar o nosso tratador de erro no Login Básico
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint()));

        return http.build();
    }
}