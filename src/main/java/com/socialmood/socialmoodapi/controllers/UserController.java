package com.socialmood.socialmoodapi.controllers;

import com.socialmood.socialmoodapi.dto.UserDetailsResponseDTO;
import com.socialmood.socialmoodapi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/details/{userId}")
    public ResponseEntity<?> userDetails(@PathVariable Long userId) {
        try {
            UserDetailsResponseDTO user = userService.findUserDetailsById(userId);

            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
            }
        } catch (Exception e) {
            log.error("Erro ao buscar usuário: ", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível realizar a busca do usuário! Tente novamente!");
    }
}
