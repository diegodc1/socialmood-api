package com.socialmood.socialmoodapi.services;

import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.repositorys.SessionRepository;
import com.socialmood.socialmoodapi.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    public Session saveSession(String nomeSession, Integer duracaoSessao, LocalDateTime inicioSessao,
                               LocalDateTime fimSessao, Long redeSocial, String emocaoPred, Long idUsuario) {
        User user = userRepository.findById(idUsuario).orElseThrow(() ->
                new RuntimeException("Usuário não encontrado com ID: " + idUsuario));

        Session session = new Session();
        session.setNome(nomeSession);
        session.setFim(fimSessao);
        session.setInicio(inicioSessao);
        session.setDuracao(duracaoSessao);
        session.setRedeSocial(redeSocial);
        session.setEmocaoPred(emocaoPred);
        session.setUsuarioId(user);

        return sessionRepository.save(session);
    }
}
