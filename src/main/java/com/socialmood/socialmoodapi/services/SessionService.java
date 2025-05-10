package com.socialmood.socialmoodapi.services;

import com.socialmood.socialmoodapi.dto.*;
import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.enums.ERedeSocial;
import com.socialmood.socialmoodapi.enums.Emotion;
import com.socialmood.socialmoodapi.repositorys.ISessionRepository;
import com.socialmood.socialmoodapi.repositorys.IUserRepository;
import com.socialmood.socialmoodapi.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SessionService {
    @Autowired
    private ISessionRepository sessionRepository;
    @Autowired
    private IUserRepository IUserRepository;
    @Autowired
    private UserService userService;

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
                User user = session.getUser();

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
                throw new RuntimeException("Usuario não autenticado");
            }

            Object princial = authentication.getPrincipal();
            String emailUsuario;
            if (princial instanceof UserDetails) {
                emailUsuario = ((UserDetails) princial).getUsername();
            } else {
                emailUsuario = princial.toString();
            }
            User user = (User) IUserRepository.findByEmail(emailUsuario);
            if(user  == null){
                throw new RuntimeException("Usuario não encontrado");
            }
            List<Session> sessions = sessionRepository.findByUserOrderByInicioDesc(user);
            Set<String> allEmotions = new HashSet<>();
            sessions.forEach(session -> {
                if (session.getEmotionsDetected() != null){
                    session.getEmotionsDetected().forEach(emotionDetected ->{
                        allEmotions.add(emotionDetected.getDataDeteccao().toString());
                    });
                }
            });
            return  sessions.stream().map(this::convertToSessionDetailsDTO).collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException("Falha na Requisição");
        }
    }


    public List<SessionFormatDTO> getListByUserId(Long userId) {
        try {
            User user = IUserRepository.findById(userId).orElse(null);
            if (user != null) {
                List<Session> listSessions = sessionRepository.findByUser(user);
                return listSessions.stream()
                        .map(SessionFormatDTO::new)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Não foi possível realizar a busca das sessões do usuario: ", e);
        }
        return null;
    }

    public SessionFormatDTO getSessionById(Long sessionId) {
        try {
            if (sessionId != null) {
                Session session = sessionRepository.findById(sessionId).orElse(null);

                if (session != null) {
                    return new SessionFormatDTO(session);
                }
            }
        } catch (Exception e) {
            log.error("Não foi possível realizar a busca da sessão: ", e);
        }
        return null;
    }

    private SessionDetailsReportDTO convertToSessionDetailsDTO_Report(Session session) {
        List<EmotionDTO> emotions = Optional.ofNullable(session.getEmotionsDetected())
                .orElse(Collections.emptyList())
                .stream()
                .map(e -> new EmotionDTO(e.getEmocao().toString(), e.getDataDeteccao().toInstant()))
                .collect(Collectors.toList());
        User user = session.getUser();

        return new SessionDetailsReportDTO(
                session.getId(),
                session.getNome(),
                session.getEmocaoPred(),
                session.getDuracao(),
                session.getInicio(),
                session.getFim(),
                session.getRedeSocial(),
                emotions,
                user.getNome(),
                user.getSobrenome(),
                user.getEmail()


        );
    }
    public List<SessionDetailsReportDTO> getAllSessionsDetailsWithUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new RuntimeException("Usuario não autenticado");
            }

            Object princial = authentication.getPrincipal();
            String emailUsuario;
            if (princial instanceof UserDetails) {
                emailUsuario = ((UserDetails) princial).getUsername();
            } else {
                emailUsuario = princial.toString();
            }
            User user = (User) IUserRepository.findByEmail(emailUsuario);
            if(user  == null){
                throw new RuntimeException("Usuario não encontrado");
            }
            List<Session> sessions = sessionRepository.findByUserOrderByInicioDesc(user);
            Set<String> allEmotions = new HashSet<>();
            sessions.forEach(session -> {
                if (session.getEmotionsDetected() != null){
                    session.getEmotionsDetected().forEach(emotionDetected ->{
                        allEmotions.add(emotionDetected.getDataDeteccao().toString());
                    });
                }
            });
            return  sessions.stream().map(this::convertToSessionDetailsDTO_Report).collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException("Falha na Requisição");
        }
    }

    public List<SessionFormatDTO> getListByFilters(ReportBodyRequestDTO filters) {
        try {
            if (filters != null) {
                Emotion emotion = Emotion.fromPortuguese(filters.emotion());
                ERedeSocial redeSocial = ERedeSocial.getSocialIdByName(filters.socialMedia());

                LocalDate inicioSessao = null;
                if (filters.dateInic() != null) {
                    inicioSessao = DateUtils.convertToLocalDate((filters.dateInic()));
                }

                List<Session> listSessions = sessionRepository.buscarPorFiltros(filters.userId(), emotion.name(), redeSocial.getName(), inicioSessao);


                return listSessions.stream()
                        .map(SessionFormatDTO::new)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Não foi possível realizar a busca das sessões do usuario: ", e);
        }
        return null;
    }
}
