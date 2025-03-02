package com.example.sentimentanalyzer.models;

import lombok.Data;

@Data
public class SocialMediaPost {
    private String username;
    private String text;
    private String timestamp;
}
