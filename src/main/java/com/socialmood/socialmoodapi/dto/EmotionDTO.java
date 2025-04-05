package com.socialmood.socialmoodapi.dto;
import java.time.Instant;

public record EmotionDTO(
        String emotion,
        Instant time
) {}
