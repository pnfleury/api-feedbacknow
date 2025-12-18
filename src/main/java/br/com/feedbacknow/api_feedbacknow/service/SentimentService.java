package br.com.feedbacknow.api_feedbacknow.service;

import br.com.feedbacknow.api_feedbacknow.domain.SentimentType;
import br.com.feedbacknow.api_feedbacknow.dto.SentimentoResponse;
import br.com.feedbacknow.api_feedbacknow.entity.SentimentEntity;
import br.com.feedbacknow.api_feedbacknow.repository.SentimentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SentimentService {

    private final SentimentRepository repository;

    public SentimentService(SentimentRepository repository) {
        this.repository = repository;
    }

    public SentimentEntity saveSentiment(SentimentoResponse response) {

        // ira converter string para enum
        SentimentType tipo;
        try {
            tipo = SentimentType.valueOf(response.getSentimento().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Sentimento inválido: " + response.getSentimento());
        }


        SentimentEntity entity = new SentimentEntity();
        entity.setComentario(response.getComentario());
        entity.setSentimento(tipo);
        entity.setProbabilidade(response.getProbabilidade());
        entity.setCriadoEm(LocalDateTime.now());

        return repository.save(entity);
    }
}
