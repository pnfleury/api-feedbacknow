package br.com.feedbacknow.api_feedbacknow.controller;


import br.com.feedbacknow.api_feedbacknow.dto.SentimentoRequest;
import br.com.feedbacknow.api_feedbacknow.dto.SentimentoResponse;
import br.com.feedbacknow.api_feedbacknow.service.SentimentoAnalyzer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SentimentoController {

    private final SentimentoAnalyzer sentimentoAnalyzer;

    public SentimentoController(SentimentoAnalyzer sentimentoAnalyzer) {
        this.sentimentoAnalyzer = sentimentoAnalyzer;
    }

    @PostMapping("/comentario")
    public ResponseEntity<SentimentoResponse> analyze(@Valid @RequestBody SentimentoRequest request) {

        // Chama o serviço que se comunica com o microserviço Python
        SentimentoResponse response = sentimentoAnalyzer.analyzeComment(request.getComentario());

        if (response == null) {
            // Retorna erro se a comunicação falhou ou o Python retornou algo inesperado
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        // Retorna a resposta do Python com status 200 OK
        return ResponseEntity.ok(response);
    }
}
