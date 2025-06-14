package com.socialmood.socialmoodapi.dto;

import com.socialmood.socialmoodapi.enums.UserRole;

import java.util.Date;

public record UserDetailsResponseDTO(Long userId, String nome, String sobrenome, String email, UserRole role, String fotoPerfil, String telefone, String genero, Date dataNascimento) {
}
