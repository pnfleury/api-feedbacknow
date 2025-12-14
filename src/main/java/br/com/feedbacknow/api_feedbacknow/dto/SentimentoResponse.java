package br.com.feedbacknow.api_feedbacknow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Representa o corpo JSON que o Python retorna, ex: {"sentimento": "positivo", "probability": 0.95}
@Data
@NoArgsConstructor // Necessário para Jackson/deserialização
@AllArgsConstructor // Útil para criar objetos facilmente
public class SentimentoResponse {

    private String comentario;
    private String sentimento; // Ex: "positivo", "negativo", "neutro"
    private double probabilidade;
}