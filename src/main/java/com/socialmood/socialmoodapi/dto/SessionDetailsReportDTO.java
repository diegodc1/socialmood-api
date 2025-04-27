package com.socialmood.socialmoodapi.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SessionDetailsReportDTO(
        Long sessionId,
        String nomeSessao,
        String emocaoPred,
        Integer duracaoSessao,
        LocalDateTime inicioSessao,
        LocalDateTime fimSessao,
        Long redeSocial,
        List<EmotionDTO> emotions,
        String name,
        String sobrenome,
        String email
) {
}
