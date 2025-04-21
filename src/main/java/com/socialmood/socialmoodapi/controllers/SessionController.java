package com.socialmood.socialmoodapi.controllers;

import com.socialmood.socialmoodapi.dto.SessionDTO;
import com.socialmood.socialmoodapi.dto.SessionDetailsDTO;
import com.socialmood.socialmoodapi.dto.SessionFormatDTO;
import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.services.EmotionDetectedService;
import com.socialmood.socialmoodapi.services.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping ("/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @Autowired
    EmotionDetectedService emotionDetectedService;


    @PostMapping("/newSession")
    public ResponseEntity<String> newSession(@RequestBody SessionDTO sessionDTO){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated()){
                return ResponseEntity.status(401).body("Usuário não autenticado");
            }
            Session session = sessionService.saveSession(
                    sessionDTO.nomeSessao(),
                    sessionDTO.duracaoSessao(),
                    sessionDTO.inicioSessao(),
                    sessionDTO.fimSessao(),
                    sessionDTO.redeSocial(),
                    sessionDTO.listEmocoes()
            );

            Boolean result = emotionDetectedService.saveEmotions(session, session.getUser(), sessionDTO.listEmocoes());
            if (result) {
                return ResponseEntity.ok().body("Criado com sucesso");
            }
        } catch (Exception e){
            log.error("Ocorrou um erro ao salvar a sessão!", e);
        }
        return ResponseEntity.internalServerError().body("Erro ao processar a requisição!");
    }


    @GetMapping("/details/current")
    public SessionDetailsDTO getLastSessionDetails() {
        return sessionService.getLastSessionDetails();
    }

    @GetMapping("/allSesions")
    public ResponseEntity<List<SessionDetailsDTO>> getAllSessionsDetails(){
        List<SessionDetailsDTO> sessions = sessionService.getAllSessionsDetails();
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/session/{sessionId}")
    public ResponseEntity<SessionFormatDTO> getSessionById(@PathVariable Long sessionId){
        SessionFormatDTO session = sessionService.getSessionById(sessionId);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<List<SessionFormatDTO>> getAllSessionsByUserId(@PathVariable Long userId){
        List<SessionFormatDTO> sessionList = sessionService.getListByUserId(userId);
        return ResponseEntity.ok(sessionList);
    }
}
