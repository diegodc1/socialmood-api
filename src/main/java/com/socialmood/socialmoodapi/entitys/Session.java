package com.socialmood.socialmoodapi.entitys;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nomeSessao", nullable = false )
    private String nome;

    @Column(name = "duracaoSessao", nullable = false)
    private Integer duracao;

    @Column(name = "inicioSessao", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fimSessao", nullable = false)
    private LocalDateTime fim;

    @Column(name = "redeSocial", nullable = false)
    private Long redeSocial;

    @Column(name = "emocaoPred", nullable = false)
    private String emocaoPred;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuarioId;
}
