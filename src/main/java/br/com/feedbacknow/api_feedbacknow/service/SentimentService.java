    package br.com.feedbacknow.api_feedbacknow.service;

    import br.com.feedbacknow.api_feedbacknow.domain.SentimentType;
    import br.com.feedbacknow.api_feedbacknow.dto.SentimentoResponse;
    import br.com.feedbacknow.api_feedbacknow.dto.StatsResponse;
    import br.com.feedbacknow.api_feedbacknow.entity.SentimentEntity;
    import br.com.feedbacknow.api_feedbacknow.repository.SentimentRepository;
    import org.springframework.stereotype.Service;

    import java.math.BigDecimal;
    import java.math.RoundingMode;
    import java.time.LocalDateTime;

    @Service
    public class SentimentService {

        private final SentimentRepository repository;

        public SentimentService(SentimentRepository repository) {
            this.repository = repository;
        }

        // MÉTODO PARA SALVAR NO BANCO
        public SentimentEntity saveSentiment(SentimentoResponse response) {

            SentimentType tipo = SentimentType.valueOf(response.getSentimento().toUpperCase());
            // 1. Converter a lista ["palavra1", "palavra2"] em "palavra1, palavra2"
            String topFeaturesString = "";
            if (response.getTopFeatures() != null && !response.getTopFeatures().isEmpty()) {
                topFeaturesString = String.join(", ", response.getTopFeatures());
            }

            SentimentEntity entity = new SentimentEntity();
            entity.setComentario(response.getComentario());
            entity.setSentimento(tipo);
            entity.setProbabilidade(response.getProbabilidade());
            entity.setCriadoEm(LocalDateTime.now());
            entity.setTopFeatures(topFeaturesString);

        return repository.save(entity);
    }

    // MÉTODO PARA GERAR ESTATÍSTICAS GERAIS OU POR DIAS
    public StatsResponse obterEstatisticas(Integer dias) {
        long total;
        long pos;
        long neg;

        if (dias == null || dias <= 0) {
            // Lógica para o BANCO TODO
            total = repository.count();
            pos = repository.countBySentimento(SentimentType.POSITIVO);
            neg = repository.countBySentimento(SentimentType.NEGATIVO);
        } else {
            // Lógica para o FILTRO DE DIAS
            LocalDateTime dataCorte = LocalDateTime.now().minusDays(dias);
            total = repository.countByCriadoEmAfter(dataCorte);
            pos = repository.countBySentimentoAndCriadoEmAfter(SentimentType.POSITIVO, dataCorte);
            neg = repository.countBySentimentoAndCriadoEmAfter(SentimentType.NEGATIVO, dataCorte);
        }

        if (total == 0) return new StatsResponse(0, 0, 0, 0.0, 0.0);

        // Cálculo com arredondamento garantido para 2 casas
        double percPos = BigDecimal.valueOf((pos * 100.0) / total)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        double percNeg = BigDecimal.valueOf((neg * 100.0) / total)
                .setScale(2,RoundingMode.HALF_UP)
                .doubleValue();

        return new StatsResponse(total, pos, neg, percPos, percNeg);
    }
}