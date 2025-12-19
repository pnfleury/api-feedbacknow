package br.com.feedbacknow.api_feedbacknow.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.HttpStatus;

import java.util.Objects;


@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    // 1. ERROS DE VALIDAÇÃO (Campos inválidos no JSON)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDetalhe> handleValidation(MethodArgumentNotValidException ex) {
        String mensagem = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        String campo = ex.getBindingResult().getFieldError().getField();
        // Usamos WARN porque é um erro de cliente, não do servidor.
        logger.warn("Validação falhou no campo '{}': {}", campo, mensagem);
        return ResponseEntity.badRequest()
                .body(new ErroDetalhe("Dados inválidos", mensagem, 400));
    }

    // 2. ERROS DE CONEXÃO (Flask desligado)
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErroDetalhe> handleConnection(ResourceAccessException ex) {
        logger.error("Falha ao conectar no Flask: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErroDetalhe("IA Indisponível", "serviço de analise fora do ar.", 503));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroDetalhe> handleInvalidJson(HttpMessageNotReadableException ex) {
        logger.warn("Tentativa de requisição com JSON inválido: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(new ErroDetalhe(
                        "JSON Inválido",
                        "O corpo da requisição está ausente ou contém erros de sintaxe (verifique aspas e chaves).",
                        400
                ));
    }

        // 3. QUALQUER OUTRO ERRO
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDetalhe> handleGeneric (Exception ex){
        logger.error("Erro interno não esperado: ", ex);
        return ResponseEntity.internalServerError()
                .body(new ErroDetalhe(
                        "Erro Interno",
                        "Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde.",
                        500
                    ));
        }
    }


