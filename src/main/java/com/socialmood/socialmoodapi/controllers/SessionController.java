package com.socialmood.socialmoodapi.controllers;

import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping ("/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;


    @PostMapping("/newSession")
    public ResponseEntity<String> newSession(@RequestBody Map<String, String> request) {

        System.out.println("Recebido: " + request);

        if (!request.containsKey("nomeSessao") || !request.containsKey("duracaoSessao") ||
                !request.containsKey("inicioSessao") || !request.containsKey("fimSessao") ||
                !request.containsKey("redeSocial") || !request.containsKey("emocaoPred") ||
                !request.containsKey("idUsuario")) {
            return ResponseEntity.badRequest().body("Erro: Campos obrigatórios ausentes.");
        }

        try {
            String nomeSessao = request.get("nomeSessao");
            Integer duracaoSessao = Integer.valueOf(request.get("duracaoSessao"));
            LocalDateTime inicioSessao = LocalDateTime.parse(request.get("inicioSessao"));
            LocalDateTime fimSessao = LocalDateTime.parse(request.get("fimSessao"));
            Long redeSocial = Long.parseLong(request.get("redeSocial"));
            String emocaoPred = request.get("emocaoPred");
            Long idUsuario = Long.parseLong(request.get("idUsuario"));

            sessionService.saveSession(nomeSessao, duracaoSessao, inicioSessao, fimSessao, redeSocial, emocaoPred, idUsuario);
            
            return ResponseEntity.ok("Sessão criada com sucesso!");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Erro ao converter números. Verifique os valores enviados.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar a requisição: " + e.getMessage());
        }
    }


}
