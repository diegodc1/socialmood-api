package com.socialmood.socialmoodapi.controllers;

import com.socialmood.socialmoodapi.dto.SessionDTO;
import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping ("/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;


    @PostMapping("/newSession")
    public ResponseEntity<String> newSession(@RequestBody SessionDTO sessionDTO){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated()){
                return ResponseEntity.status(401).body("usuario não autenticado");
            }
            sessionService.saveSession(
                    sessionDTO.nomeSessao(),
                    sessionDTO.duracaoSessao(),
                    sessionDTO.inicioSessao(),
                    sessionDTO.fimSessao(),
                    sessionDTO.redeSocial(),
                    sessionDTO.emocaoPred()
            );
            return ResponseEntity.ok().body("Criado com sucesso");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Erro ao processar a requisição: " + e.getMessage());
        }
    }


}
