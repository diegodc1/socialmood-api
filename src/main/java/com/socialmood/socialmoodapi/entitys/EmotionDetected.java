package com.socialmood.socialmoodapi.entitys;

import com.socialmood.socialmoodapi.enums.Emotion;
import com.socialmood.socialmoodapi.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
@Getter
@Entity(name = "emotion_detected")
@Table(name = "emotion_detected")
public class EmotionDetected {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private Emotion emocao;

    @Column(name = "emod_data_deteccao", nullable = false)
    private Date dataDeteccao;

    @ManyToOne
    @JoinColumn(name = "emod_usuario", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "emod_sessao", nullable = false)
    private Session sessao;
}
