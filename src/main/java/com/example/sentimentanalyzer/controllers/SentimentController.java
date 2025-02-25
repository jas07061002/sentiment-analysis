package com.example.sentimentanalyzer.controllers;

import com.example.sentimentanalyzer.models.SentimentRequest;
import com.example.sentimentanalyzer.models.SentimentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class SentimentController {

    @PostMapping("/analyze-sentiment")
    public ResponseEntity<SentimentResponse> analyzeSentiment(@RequestBody SentimentRequest request) {
        log.info("Received sentiment analysis request: {}", request.getText());
        try {
            String text = request.getText().toLowerCase();
            String sentiment = text.contains("happy") || text.contains("good") ? "positive" :
                    text.contains("sad") || text.contains("bad") ? "negative" : "neutral";

            SentimentResponse response = new SentimentResponse(sentiment, "Analyzed successfully");
            log.info("Returning sentiment: {}", sentiment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing sentiment analysis", e);
            return ResponseEntity.status(500).body(new SentimentResponse("error", e.getMessage()));
        }
    }
}



