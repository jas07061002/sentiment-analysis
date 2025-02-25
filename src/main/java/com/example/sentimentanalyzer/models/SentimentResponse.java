package com.example.sentimentanalyzer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SentimentResponse {
    private String sentiment;
    private String message;

}