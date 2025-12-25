    package br.com.feedbacknow.api_feedbacknow.service;

    import br.com.feedbacknow.api_feedbacknow.domain.SentimentType;
    import br.com.feedbacknow.api_feedbacknow.dto.SentimentoResponse;
    import br.com.feedbacknow.api_feedbacknow.dto.StatsResponse;
    import br.com.feedbacknow.api_feedbacknow.domain.SentimentEntity;
    import br.com.feedbacknow.api_feedbacknow.repository.SentimentRepository;

    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;

    import java.math.BigDecimal;
    import java.math.RoundingMode;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Optional;

    @Service
    public class SentimentService {

        private final SentimentRepository repository;

        public SentimentService(SentimentRepository repository) {
            this.repository = repository;
        }

        // MÉTODO PARA SALVAR NO BANCO
        public SentimentoResponse saveSentiment(SentimentoResponse response) {

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

            SentimentEntity salva = repository.save(entity);
            // Antes de sair, atualiza o DTO original com o ID gerado e a data
            response.setId(salva.getId());
            response.setTimestamp(salva.getCriadoEm());

            return response;
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

        // METODO PARA LISTAR TODOS OS REGISTROS
        public Page<SentimentoResponse> listarTodos(Pageable paginacao) {
            return repository.findAll(paginacao).map(this::mapToResponse);
        }

        // METODO PARA BUSCAR POR ID
        public Optional<SentimentoResponse> buscarPorId(Long id) {
            return repository.findById(id).map(this::mapToResponse);
        }

        // METODO PARA BUSCAR SENTIMENTO
        public Page<SentimentoResponse> buscarPorSentimento(SentimentType sentimento, Pageable pageable) {
            return repository.findBySentimento(sentimento, pageable)
                    .map(this::mapToResponse); // Converte cada item da página
        }

        // MÉTODO PRIVADO DE CONVERSÃO (O mapeador centralizado)
        private SentimentoResponse mapToResponse(SentimentEntity entity) {
            return new SentimentoResponse(
                    entity.getId(),
                    entity.getComentario(),
                    entity.getSentimento().name(),
                    entity.getProbabilidade(),
                    entity.getTopFeatures() != null ? List.of(entity.getTopFeatures().split(",")) : null,
                    entity.getCriadoEm()
            );
        }
}
