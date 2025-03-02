/*
package com.example.sentimentanalyzer.service;

import com.example.sentimentanalyzer.exception.TokenRetrievalException;
import com.example.sentimentanalyzer.models.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class KeycloakTokenService {

    private final RestTemplate restTemplate;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    public KeycloakTokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessToken() {
        log.info("Fetching token from Keycloak with client_id: {}", clientId);

        HttpEntity<MultiValueMap<String, String>> request = buildRequest();

        return fetchToken(request)
                .map(TokenResponse::getAccessToken)
                .orElseThrow(() -> new TokenRetrievalException("Failed to obtain access token: Empty response"));
    }

    private HttpEntity<MultiValueMap<String, String>> buildRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        return new HttpEntity<>(body, headers);
    }

    private Optional<TokenResponse> fetchToken(HttpEntity<MultiValueMap<String, String>> request) {
        try {
            TokenResponse response = restTemplate.postForObject(tokenUri, request, TokenResponse.class);
            if (response != null && response.getAccessToken() != null) {
                log.info("Token fetched successfully, expires in: {} seconds", response.getExpiresIn());
                return Optional.of(response);
            } else {
                log.error("Token response is null or missing access_token");
                return Optional.empty();
            }
        } catch (HttpClientErrorException e) {
            log.error("Failed to fetch token: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new TokenRetrievalException("Token fetch failed: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error fetching token", e);
            throw new TokenRetrievalException("Unexpected error: " + e.getMessage(), e);
        }
    }
}

*/
