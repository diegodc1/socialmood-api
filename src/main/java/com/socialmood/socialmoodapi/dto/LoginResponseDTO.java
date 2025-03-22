package com.socialmood.socialmoodapi.dto;

import com.socialmood.socialmoodapi.enums.UserRole;

public record LoginResponseDTO(String token, Long userId, String userName, String userEmail, UserRole userRole) {
}
