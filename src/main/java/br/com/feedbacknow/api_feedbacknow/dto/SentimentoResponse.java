package br.com.feedbacknow.api_feedbacknow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// Representa o corpo JSON que o Python retorna, ex: {"sentimento": "positivo", "probability": 0.95}
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentoResponse {

    private Long id;
    private String comentario;
    private String sentimento; // Ex: "positivo" ou "negativo"
    private double probabilidade;
    private List<String> topFeatures;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")// determina data e hora apenas
    private LocalDateTime timestamp;
}