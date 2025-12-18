package br.com.feedbacknow.api_feedbacknow.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Sentiment {

    private SentimentType sentiment;

    private LocalDateTime createdAt = LocalDateTime.now();
}
