package br.com.feedbacknow.api_feedbacknow.controller;


import br.com.feedbacknow.api_feedbacknow.dto.SentimentoRequest;
import br.com.feedbacknow.api_feedbacknow.dto.SentimentoResponse;
import br.com.feedbacknow.api_feedbacknow.repository.SentimentRepository;
import br.com.feedbacknow.api_feedbacknow.service.SentimentService;
import br.com.feedbacknow.api_feedbacknow.service.SentimentoAnalyzer;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class SentimentoController {

    private final SentimentoAnalyzer sentimentoAnalyzer;
    private final SentimentService sentimentService;
    private final SentimentRepository repository;


    public SentimentoController(SentimentoAnalyzer sentimentoAnalyzer, SentimentService sentimentService, SentimentRepository repository) {
        this.sentimentoAnalyzer = sentimentoAnalyzer;
        this.sentimentService = sentimentService;
        this.repository = repository;
    }

    @PostMapping("/sentiment")
    public ResponseEntity<SentimentoResponse> analyze(@Valid @RequestBody SentimentoRequest request) {

        // Chama o serviço que se comunica com o microserviço Python
        SentimentoResponse response = sentimentoAnalyzer.analyzeComment(request.getComentario());

        if (response == null) {
            // Retorna erro se a comunicação falhou ou o Python retornou algo inesperado
            return ResponseEntity.
                    status(HttpStatus.SERVICE_UNAVAILABLE)
                    .build();
        }

        //define o comentario
        response.setComentario(request.getComentario());
        //define (data e hora)
        response.setTimestamp(LocalDateTime.now());
        // salva as informacoes no banco
        var entidadeSalva = sentimentService.saveSentiment(response);

        response.setId(entidadeSalva.getId());
        // Retorna a resposta do Python com status 200 OK
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sentiments")
    public Page<SentimentoResponse> listar(
            @PageableDefault(page = 0, size = 10, sort = {"criadoEm"})
            Pageable paginacao) {

        return repository.findAll(paginacao)
                .map(entity -> new SentimentoResponse(
                        entity.getId(),
                        entity.getComentario(),
                        entity.getSentimento().name(), //converte o enum para String
                        entity.getProbabilidade(),
                        entity.getCriadoEm()
                ));
    }

    @GetMapping("/sentiment/{id}")// http://localhost:8080/sentiment/1
    public ResponseEntity<SentimentoResponse> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(entity -> ResponseEntity.ok(new SentimentoResponse(
                        entity.getId(),
                        entity.getComentario(),
                        entity.getSentimento().name(),
                        entity.getProbabilidade(),
                        entity.getCriadoEm()
                )))
                .orElse(ResponseEntity.notFound().build());
    }


}
