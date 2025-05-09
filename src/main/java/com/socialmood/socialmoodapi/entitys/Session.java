package com.socialmood.socialmoodapi.entitys;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_sessao", nullable = false )
    private String nome;

    @Column(name = "duracao_sessao", nullable = false)
    private Integer duracao;

    @Column(name = "inicio_sessao", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fim_sessao", nullable = false)
    private LocalDateTime fim;

    @Column(name = "rede_social", nullable = false)
    private Long redeSocial;

    @Column(name = "emocao_pred", nullable = false)
    private String emocaoPred;

    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmotionDetected> emotionsDetected;

    public List<EmotionDetected> getEmotionDetecteds() {
        return emotionsDetected;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;
}
