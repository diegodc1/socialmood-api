package com.socialmood.socialmoodapi.dto;

import com.socialmood.socialmoodapi.entitys.EmotionDetected;
import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.enums.ERedeSocial;
import com.socialmood.socialmoodapi.enums.Emotion;
import com.socialmood.socialmoodapi.utils.FormatUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
public class SessionFormatDTO {
    private Long sessionId;
    private String nomeSessao;
    private String emocaoPredominante;
    private Integer duracaoSessao;
    private String duracaoSessaoFormatado;
    LocalDateTime inicioSessao;
    LocalDateTime fimSessao;
    Long redeSocialCod;
    String redeSocialNome;
    List<EmotionDTO> emocoes;

    public SessionFormatDTO(Long sessionId, String nomeSessao, String emocaoPredominante, Integer duracaoSessao, String duracaoSessaoFormatado, LocalDateTime inicioSessao, LocalDateTime fimSessao, Long redeSocialCod, Long redeSocialNome, List<EmotionDTO> emotions) {
        this.sessionId = sessionId;
        this.nomeSessao = nomeSessao;
        this.emocaoPredominante = FormatUtils.formatEmocaoName(emocaoPredominante);
        this.duracaoSessao = duracaoSessao;
        this.duracaoSessaoFormatado = FormatUtils.formatSeconds(duracaoSessao);
        this.inicioSessao = inicioSessao;
        this.fimSessao = fimSessao;
        this.redeSocialCod = redeSocialCod;
        this.redeSocialNome = ERedeSocial.getSocialName(redeSocialCod).getName();
        this.emocoes = emotions;
    }

    public SessionFormatDTO(Session session) {
        this.sessionId = session.getId();
        this.nomeSessao = session.getNome();
        this.emocaoPredominante = FormatUtils.formatEmocaoName(session.getEmocaoPred());
        this.duracaoSessao = session.getDuracao();
        this.duracaoSessaoFormatado = FormatUtils.formatSeconds(session.getDuracao());
        this.inicioSessao = session.getInicio();
        this.fimSessao = session.getFim();
        this.redeSocialCod = session.getRedeSocial();
        this.redeSocialNome = ERedeSocial.getSocialName(session.getRedeSocial()).getName();
        this.emocoes = Optional.ofNullable(session.getEmotionsDetected())
                .orElse(Collections.emptyList())
                .stream()
                .map(e -> new EmotionDTO(e.getEmocao().toString(), e.getDataDeteccao().toInstant()))
                .collect(Collectors.toList());
    }
}
