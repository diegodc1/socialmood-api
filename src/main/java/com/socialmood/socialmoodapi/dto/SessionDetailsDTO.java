package com.socialmood.socialmoodapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SessionDetailsDTO(
        Long sessionId,
        String nomeSessao,
        String emocaoPred,
        Integer duracaoSessao,
        LocalDateTime inicioSessao,
        LocalDateTime fimSessao,
        Long redeSocial,
        List<EmotionDTO> emotions



) {
}
