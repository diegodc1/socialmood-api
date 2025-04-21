package com.socialmood.socialmoodapi.controllers;

import com.socialmood.socialmoodapi.dto.UpdatePasswordDTO;
import com.socialmood.socialmoodapi.dto.UpdateUserDTO;
import com.socialmood.socialmoodapi.dto.UserDetailsResponseDTO;
import com.socialmood.socialmoodapi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update/email")
    public ResponseEntity<?> updateUserEmail(@RequestBody UpdateUserDTO updateDto) {
        Boolean result = userService.updateUserEmail(updateDto.userId(), updateDto.value());

        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar o usuário!");
    }

    @PutMapping("/update/phone")
    public ResponseEntity<?> updateUserPhone(@RequestBody UpdateUserDTO updateDto) {
        Boolean result = userService.updateUserPhone(updateDto.userId(), updateDto.value());

        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar o usuário!");
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestBody UpdatePasswordDTO passwordDTO) {
        String result = userService.updateUserPassword(passwordDTO);

        return switch (result) {
            case "200" -> ResponseEntity.status(HttpStatus.OK).body("Usuário atualizado com sucesso!");
            case "404" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
            case "401" -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha atual incorreta!");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao atualizar o usuário!");
        };

    }
}
