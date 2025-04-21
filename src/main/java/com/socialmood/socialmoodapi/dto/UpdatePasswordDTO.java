package com.socialmood.socialmoodapi.dto;

public record UpdatePasswordDTO(Long userId, String currentPassword, String newPassword) {
}
