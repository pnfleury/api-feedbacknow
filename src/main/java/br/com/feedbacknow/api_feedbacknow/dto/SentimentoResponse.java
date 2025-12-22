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
@NoArgsConstructor // Necessário para Jackson/deserialização
@AllArgsConstructor // Útil para criar objetos facilmente
public class SentimentoResponse {

    //@JsonProperty("comentario")
    private String comentario;

    private String sentimento; // Ex: "positivo" ou "negativo"
    private double probabilidade;

    // Teste de implementação do TopFeatures
    private List<String> topFeatures;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")// determina data e hora apenas
    private LocalDateTime timestamp;
}