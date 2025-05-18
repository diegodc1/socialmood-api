package com.socialmood.socialmoodapi.dto;

import com.socialmood.socialmoodapi.enums.UserRole;

import java.util.Date;

public record RegisterDTO(
        String nome, String sobrenome, String email, String senha, String telefone, String genero, Date dataNascimento, UserRole role) {
}
