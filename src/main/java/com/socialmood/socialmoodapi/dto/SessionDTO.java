package com.socialmood.socialmoodapi.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;


public record SessionDTO(
        String nomeSessao, Integer duracaoSessao, LocalDateTime inicioSessao, LocalDateTime fimSessao, Long redeSocial,  List<EmotionDTO> listEmocoes, Long userId
) {}
