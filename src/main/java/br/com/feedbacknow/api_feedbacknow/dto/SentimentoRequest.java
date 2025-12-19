package br.com.feedbacknow.api_feedbacknow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Representa o corpo JSON que o Python espera, ex: {"comentario": "Texto a ser analisado"}
@Data
@NoArgsConstructor // Necessário para Jackson/deserialização
@AllArgsConstructor // Útil para criar objetos facilmente
public class SentimentoRequest {

    // O nome do campo deve ser EXATAMENTE igual à chave JSON que o Flask espera.
    @NotBlank(message = "A reclamação, sugestão ou elogios é obrigatória")
    @Size(min = 5, max = 500, message = "A informação deve ter entre 5 e 500 caracteres")
    @Pattern(
            regexp = "^[\\p{L}\\p{N}\\p{P}\\p{Zs}]+$",
            message = "A mensagem não pode conter emojis ou ícones!"
    )
    private String comentario;
}