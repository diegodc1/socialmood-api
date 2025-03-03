package com.socialmood.socialmoodapi.entitys;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String nome;

    @Column(name = "user_lastname")
    private String sobrenome;

    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name = "user_password")
    private String senha;

    @Column(name = "user_photo")
    private String fotoPerfil;

    @Column(name = "user_phone")
    private String telefone;

    @Column(name = "user_gender")
    private String genero;

}
