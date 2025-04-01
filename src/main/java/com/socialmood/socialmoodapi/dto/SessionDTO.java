package com.socialmood.socialmoodapi.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record SessionDTO(String nomeSessao,
                         Integer duracaoSessao,
                         LocalDateTime inicioSessao,
                         LocalDateTime fimSessao,
                         Long redeSocial,
                         String emocaoPred)
                        {
}
