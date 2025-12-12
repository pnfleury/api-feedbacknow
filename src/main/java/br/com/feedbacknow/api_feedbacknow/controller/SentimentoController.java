package br.com.feedbacknow.api_feedbacknow.controller;

import br.com.feedbacknow.api_feedbacknow.dto.dadosCadastroSentimento;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sentimento")
public class SentimentoController {

    @PostMapping
    public void sentimento(@RequestBody dadosCadastroSentimento dados){
        System.out.println(dados);

    }
}
