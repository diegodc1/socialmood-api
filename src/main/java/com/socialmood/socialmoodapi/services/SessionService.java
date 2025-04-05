package com.socialmood.socialmoodapi.services;

import com.socialmood.socialmoodapi.dto.EmotionDTO;
import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.repositorys.ISessionRepository;
import com.socialmood.socialmoodapi.repositorys.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class SessionService {
    @Autowired
    private ISessionRepository sessionRepository;
    @Autowired
    private IUserRepository IUserRepository;

    public Session saveSession(String nomeSession, Integer duracaoSessao, LocalDateTime inicioSessao,
                               LocalDateTime fimSessao, Long redeSocial,  List<EmotionDTO> listEmocoes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()){
                throw new RuntimeException("Usuario não autenticado");
            }

        Object princial = authentication.getPrincipal();
            String emailUsuario;
       if (princial instanceof UserDetails){
           emailUsuario = ((UserDetails) princial).getUsername();
       }else {
           emailUsuario = princial.toString();
       }

        ZoneId zoneBR = ZoneId.of("America/Sao_Paulo");

        // Considera que o inicioSessao recebido veio em UTC
        LocalDateTime inicioConvertido = inicioSessao.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(zoneBR)
                .toLocalDateTime();

        LocalDateTime fimConvertido = fimSessao.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(zoneBR)
                .toLocalDateTime();



       User user  = (User) princial;

        Session session = new Session();
        session.setNome(nomeSession);
        session.setInicio(inicioConvertido);
        session.setFim(fimConvertido);
        session.setDuracao(duracaoSessao);
        session.setRedeSocial(redeSocial);
        session.setEmocaoPred(verifyEmotionPred(listEmocoes));
        session.setUser(user);

        return sessionRepository.save(session);
    }


    private String verifyEmotionPred( List<EmotionDTO> listEmocoes) {
        Map<String, Integer> emotionCount = new HashMap<>();

        for (EmotionDTO emotion : listEmocoes) {
            emotionCount.put(emotion.emotion(), emotionCount.getOrDefault(emotion.emotion(), 0) + 1);
        }

        String mostFrequentEmotion = null;
        int maxCount = 0;


        for (Map.Entry<String, Integer> entry : emotionCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentEmotion = entry.getKey();
            }
        }

        return mostFrequentEmotion;
    }
}
