package br.com.feedbacknow.api_feedbacknow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Representa o corpo JSON que o Python retorna, ex: {"sentimento": "positivo", "probability": 0.95}
@Data
@NoArgsConstructor // Necessário para Jackson/deserialização
@AllArgsConstructor // Útil para criar objetos facilmente
public class SentimentoResponse {

    private String comentario;
    private String sentimento; // Ex: "positivo", "negativo", "neutro"
    private double probabilidade;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")// determina data e hora apenas
    private LocalDateTime timestamp;
}