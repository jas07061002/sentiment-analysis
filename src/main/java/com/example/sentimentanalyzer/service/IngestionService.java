package com.example.sentimentanalyzer.service;

import com.example.sentimentanalyzer.models.SocialMediaPost;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class IngestionService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Sample sentiment keywords
    private final String[] positiveWords = {
            "happy", "good", "awesome", "love",
            "amazing", "brilliant", "cheerful", "delightful",
            "excellent", "fantastic", "grateful", "inspiring",
            "joyful", "kind", "lively", "marvelous",
            "nice", "optimistic", "peaceful", "radiant",
            "spectacular", "thrilling", "uplifting", "vibrant",
            "wonderful", "zealous"
    };
    private final String[] negativeWords = {
            "sad", "bad", "terrible", "hate",
            "awful", "horrible", "miserable", "painful",
            "angry", "disgusting", "dreadful", "frustrating",
            "gloomy", "hopeless", "nasty", "pathetic",
            "resentful", "rude", "shameful", "tragic",
            "unhappy", "vicious", "wicked", "wretched",
            "anxious", "depressing", "fearful", "guilty"
    };

    private final String[] neutralWords = {
            "okay", "fine", "meh", "whatever",
            "average", "moderate", "indifferent", "passable",
            "mediocre", "ordinary", "fair", "tolerable",
            "uncertain", "mild", "acceptable", "standard",
            "neutral", "unremarkable", "so-so", "unimpressive",
            "undecided", "balanced", "normal", "decent",
            "reasonable", "not bad", "bland", "equitable"
    };

    private final String[] usernames = {
            "user1", "coolgal", "johndoe", "happyfeet",
            "starblazer", "mysticshadow", "chillvibes", "coderX",
            "sunnydays", "wanderlust", "midnightowl", "epicgamer",
            "bluephoenix", "silentstorm", "moonwalker", "luckystrike",
            "cyberninja", "retroking", "velvetdream", "echofox",
            "neonpulse", "quantumflux", "frostbyte", "lazygenius",
            "zenmaster", "ghostrider", "serendipity", "nightwatch"
    };

    @PostConstruct
    public void ingestPosts() {
        try {
            List<SocialMediaPost> posts = createPosts(1000);
            for (SocialMediaPost post : posts) {
                String jsonPost = objectMapper.writeValueAsString(post);
                rabbitTemplate.convertAndSend("sentiment-queue", jsonPost);
                log.info("Sent to RabbitMQ: {}", post.getText());
            }
            log.info("Ingested and sent {} mock posts to RabbitMQ", posts.size());
        } catch (Exception e) {
            log.error("Failed to ingest mock posts", e);
        }
    }

    private List<SocialMediaPost> createPosts(int count) {
        List<SocialMediaPost> posts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            posts.add(generateRandomPost());
        }
        return posts;
    }

    private SocialMediaPost generateRandomPost() {
        SocialMediaPost post = new SocialMediaPost();
        post.setUsername(usernames[random.nextInt(usernames.length)]);
        post.setTimestamp(Instant.now().minus(random.nextInt(7), ChronoUnit.DAYS).toString());

        String sentimentWord;
        int sentimentType = random.nextInt(3);
        if (sentimentType == 0) {
            sentimentWord = positiveWords[random.nextInt(positiveWords.length)];
        } else if (sentimentType == 1) {
            sentimentWord = negativeWords[random.nextInt(negativeWords.length)];
        } else {
            sentimentWord = neutralWords[random.nextInt(neutralWords.length)];
        }
        post.setText("Feeling " + sentimentWord + " today! #" + sentimentWord);

        return post;
    }
}