package br.com.feedbacknow.api_feedbacknow.repository;

import br.com.feedbacknow.api_feedbacknow.entity.SentimentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentimentRepository extends JpaRepository<SentimentEntity, Long> {
}
