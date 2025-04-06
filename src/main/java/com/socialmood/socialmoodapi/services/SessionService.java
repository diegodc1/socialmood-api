package com.socialmood.socialmoodapi.services;

import com.socialmood.socialmoodapi.dto.EmotionDTO;
import com.socialmood.socialmoodapi.dto.SessionDetailsDTO;
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
import java.util.stream.Collectors;

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


    public  SessionDetailsDTO getSessionDetaislById (Long sessionID){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            throw new RuntimeException("Usuario não autenticado");
        }

        Object principal = authentication.getPrincipal();
        String emailUser;

        if(principal instanceof UserDetails){
            emailUser = ((UserDetails) principal).getUsername();
        }else {
            emailUser = principal.toString();
        }

        User user = (User) IUserRepository.findByEmail(emailUser);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Session session = sessionRepository.findByIdAndUser(sessionID, user);
        if (session == null) {
            throw new RuntimeException("Sessão não encontrada para este usuário");
        }

        List<EmotionDTO> listEmocoes = new ArrayList<>();
        if (session.getEmotionsDetected() != null) {
            session.getEmotionsDetected().forEach(emotionDetected -> {
                EmotionDTO emotionDTO = new EmotionDTO(
                        emotionDetected.getEmocao().toString(),
                        emotionDetected.getDataDeteccao().toInstant()
                );
                listEmocoes.add(emotionDTO);
            });
        }

            SessionDetailsDTO detailsDTO = new SessionDetailsDTO(
                    session.getId(),
                    session.getNome(),
                    session.getEmocaoPred(),
                    session.getDuracao(),
                    session.getInicio(),
                    session.getFim(),
                    session.getRedeSocial(),
                    listEmocoes    );

        return detailsDTO;
  }
    public SessionDetailsDTO getLastSessionDetails() {
        Session lastSession = sessionRepository.findTopByOrderByIdDesc();
        if (lastSession == null) {
            throw new RuntimeException("Nenhuma sessão encontrada");
        }
        return getSessionDetaislById(lastSession.getId());
    }

    private SessionDetailsDTO convertToSessionDetailsDTO(Session session) {
        List<EmotionDTO> emotions = Optional.ofNullable(session.getEmotionsDetected())
                .orElse(Collections.emptyList())
                .stream()
                .map(e -> new EmotionDTO(e.getEmocao().toString(), e.getDataDeteccao().toInstant()))
                .collect(Collectors.toList());

        return new SessionDetailsDTO(
                session.getId(),
                session.getNome(),
                session.getEmocaoPred(),
                session.getDuracao(),
                session.getInicio(),
                session.getFim(),
                session.getRedeSocial(),
                emotions
        );
    }
    public List<SessionDetailsDTO> getAllSessionsDetails() {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("Acesso não autorizado - nenhum usuário autenticado");
            }

            Object principal = authentication.getPrincipal();
            String emailUser;

            if(principal instanceof UserDetails){
                emailUser = ((UserDetails) principal).getUsername();
            }else {
                emailUser = principal.toString();
            }

            User user = (User) IUserRepository.findByEmail(emailUser);
            if (user == null) {
                throw new RuntimeException("Usuário não encontrado");
            }

            List<Session> sessions = sessionRepository.findByUserOrderByInicioDesc(user);

            return sessions.stream()
                    .map(this::convertToSessionDetailsDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Erro ao buscar sessões: " + e.getMessage());
            throw new RuntimeException("Falha ao processar requisição: " + e.getMessage());
        }
    }

}
