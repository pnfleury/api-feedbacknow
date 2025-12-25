package br.com.feedbacknow.api_feedbacknow.config;

import jakarta.servlet.http.HttpServletResponse; // Import necessário para o Response
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Tratador de erros do login
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"erro\": \"Não autorizado\", \"detalhe\": \"Usuário ou senha inválidos ou ausentes.\"}");
        };
    }

    //Filter Chain definida
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))
                .csrf(AbstractHttpConfigurer::disable) // APIs REST não precisam de CSRF
                .authorizeHttpRequests(req -> {
                    // Estas linhas permitem que a TELA do Swagger carregue
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
                    req.anyRequest().authenticated();
                })
                // Spring vai usar o tratador de erro (acima) no login básico
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint()));

        return http.build();
    }
}