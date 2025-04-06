package com.socialmood.socialmoodapi.repositorys;

import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ISessionRepository extends JpaRepository<Session, Long> {
    Session findByIdAndUser(Long id, User user);
    Iterable<Long> id(Long id);
    Session findTopByOrderByIdDesc();
    List<Session> findByUserOrderByInicioDesc(User user);

    List<Session> findByUser(User user);
}
