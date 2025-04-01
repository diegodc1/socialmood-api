package com.socialmood.socialmoodapi.services;

import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.repositorys.SessionRepository;
import com.socialmood.socialmoodapi.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    public Session saveSession(String nomeSession, Integer duracaoSessao, LocalDateTime inicioSessao,
                               LocalDateTime fimSessao, Long redeSocial, String emocaoPred) {

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

       User user  = (User) princial;

        Session session = new Session();
        session.setNome(nomeSession);
        session.setFim(fimSessao);
        session.setInicio(inicioSessao);
        session.setDuracao(duracaoSessao);
        session.setRedeSocial(redeSocial);
        session.setEmocaoPred(emocaoPred);
        session.setUser(user);

        return sessionRepository.save(session);
    }
}
