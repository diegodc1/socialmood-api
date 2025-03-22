package com.socialmood.socialmoodapi.dto;

import com.socialmood.socialmoodapi.enums.UserRole;

public record RegisterDTO(String nome, String sobrenome, String email, String senha, String telefone, String genero, UserRole role) {
}
