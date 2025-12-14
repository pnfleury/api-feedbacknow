package br.com.feedbacknow.api_feedbacknow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Representa o corpo JSON que o Python espera, ex: {"comment": "Texto a ser analisado"}
@Data
@NoArgsConstructor // Necessário para Jackson/deserialização
@AllArgsConstructor // Útil para criar objetos facilmente
public class SentimentoRequest {

    // O nome do campo deve ser EXATAMENTE igual à chave JSON que o Flask espera.
    private String comentario;
}