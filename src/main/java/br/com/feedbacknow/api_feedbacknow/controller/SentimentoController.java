package br.com.feedbacknow.api_feedbacknow.controller;

import br.com.feedbacknow.api_feedbacknow.domain.SentimentType;
import br.com.feedbacknow.api_feedbacknow.dto.PaginaResponse;
import br.com.feedbacknow.api_feedbacknow.dto.SentimentoRequest;
import br.com.feedbacknow.api_feedbacknow.dto.SentimentoResponse;
import br.com.feedbacknow.api_feedbacknow.dto.StatsResponse;
import br.com.feedbacknow.api_feedbacknow.repository.SentimentRepository;
import br.com.feedbacknow.api_feedbacknow.service.SentimentService;
import br.com.feedbacknow.api_feedbacknow.service.SentimentoAnalyzer;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // Envia o comentario para a api Flask
    @PostMapping ("/sentiment")
    public ResponseEntity<SentimentoResponse> analyze(@Valid @RequestBody SentimentoRequest request) {

        // Chama o serviço que se comunica com o Flask
        SentimentoResponse response = sentimentoAnalyzer.analyzeComment(request.getComentario());

        if (response == null) {
            // Retorna erro se a comunicação falhou ou o Python retornou algo inesperado
            return ResponseEntity.
                    status(HttpStatus.SERVICE_UNAVAILABLE)
                    .build();
        }
        // salva as informacoes no banco
        sentimentService.saveSentiment(response);

        response.setTimestamp(LocalDateTime.now());
        // Retorna a resposta do Python com status 200 OK
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // BUSCA ESTATISTICAS NO BANCO
    @GetMapping("sentiments/stats")
    public ResponseEntity<StatsResponse> getStats(
            @RequestParam(required = false) Integer dias) {
        return ResponseEntity.ok(sentimentService.obterEstatisticas(dias));
    }

    // LISTA REGISTROS DO BANCO
    @GetMapping ("/sentiments")
    public ResponseEntity<PaginaResponse<SentimentoResponse>> listar(
            @ParameterObject @PageableDefault(page = 0,
                    size = 10,
                    sort = "criadoEm",
                    direction = Sort.Direction.DESC ) Pageable paginacao) {

        // Busca a página completa
        Page<SentimentoResponse> paginaResultado = sentimentService.listarTodos(paginacao);

        // Converte para o seu formato enxuto (usando os mesmos nomes que definimos antes)
        PaginaResponse<SentimentoResponse> respostaEnxuta = new PaginaResponse<>(
                paginaResultado.getContent(),
                paginaResultado.getNumber(),
                paginaResultado.getTotalElements(),
                paginaResultado.getTotalPages()
        );

        return ResponseEntity.ok(respostaEnxuta);
    }

    // LISTA REGISTRO DO BANCO POR ID
    @GetMapping("sentiment/{id}")
    public ResponseEntity<SentimentoResponse> buscarPorId(@PathVariable Long id) {
        return sentimentService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // LISTA TODOS OS REGISTROS DO BANCO COM O SENTIMENTO X
    @GetMapping("/estado/{sentimentoStr}")
    public ResponseEntity<PaginaResponse<SentimentoResponse>> filtrarPorSentimento(
            @PathVariable String sentimentoStr, // Recebe como String
            @ParameterObject @PageableDefault(size = 10,
                    page = 0,
                    sort = "criadoEm",
                    direction = Sort.Direction.DESC ) Pageable paginacao) {

        // Converte aqui para maiúsculas para bater com o Enum
        SentimentType tipo = SentimentType.valueOf(sentimentoStr.toUpperCase());

        // Pegamos a página completa do Service
        Page<SentimentoResponse> paginaResultado = sentimentService.buscarPorSentimento(tipo, paginacao);

        // Criamos a resposta enxuta manualmente
        PaginaResponse<SentimentoResponse> respostaEnxuta = new PaginaResponse<>(
                paginaResultado.getContent(),
                paginaResultado.getNumber(),
                paginaResultado.getTotalElements(),
                paginaResultado.getTotalPages()
        );

        return ResponseEntity.ok(respostaEnxuta);
    }

    @GetMapping ("/health")
        public ResponseEntity<String> health(){
            return ResponseEntity.ok("OK");
        }
}



