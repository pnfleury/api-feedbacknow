package br.com.feedbacknow.api_feedbacknow.service;

import br.com.feedbacknow.api_feedbacknow.dto.SentimentoRequest;
import br.com.feedbacknow.api_feedbacknow.dto.SentimentoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SentimentoAnalyzer {

    private final RestTemplate restTemplate;

    // Configuração do URL do serviço Python (pode ser lida do application.properties)
    @Value("${python.sentiment.url:http://localhost:5000/comentario}")
    private String pythonServiceUrl;

    public SentimentoAnalyzer(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SentimentoResponse analyzeComment(String comentario) {
        // 1. Criar o objeto de requisição (payload)
        SentimentoRequest requestBody = new SentimentoRequest(comentario);

        // 2. Configurar os headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. Criar a entidade da requisição (payload + headers)
        HttpEntity<SentimentoRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // 4. Enviar a requisição POST e mapear a resposta para SentimentResponse
            ResponseEntity<SentimentoResponse> responseEntity = restTemplate.exchange(
                    pythonServiceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    SentimentoResponse.class
            );

            // 5. Retornar o corpo da resposta
            return responseEntity.getBody();

        } catch (Exception e) {
            // Logar o erro de comunicação
            System.err.println("Erro ao se comunicar com o serviço Python em " + pythonServiceUrl + ": " + e.getMessage());
            // Em um ambiente de produção, lance uma exceção de domínio ou implemente um circuito de segurança
            return null;
        }
    }
}