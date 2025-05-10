package com.socialmood.socialmoodapi.repositorys;

import com.socialmood.socialmoodapi.entitys.Session;
import com.socialmood.socialmoodapi.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ISessionRepository extends JpaRepository<Session, Long>, JpaSpecificationExecutor<Session> {
    Session findByIdAndUser(Long id, User user);
    Iterable<Long> id(Long id);
    Session findTopByOrderByIdDesc();
    List<Session> findByUserOrderByInicioDesc(User user);
    List<Session> findByUser(User user);


    @Query(value = """
    SELECT * 
    FROM sessions
    WHERE (:emocao IS NULL OR emocao = :emocao)
      AND (:redeSocial IS NULL OR rede_social = :redeSocial)
      AND (:dataInicio IS NULL OR data >= :dataInicio)
    """, nativeQuery = true)
    List<Session> buscarPorFiltros(
            @Param("userId") Long userId,
            @Param("emocao") String emocao,
            @Param("redeSocial") String redeSocial,
            @Param("dataInicio") LocalDate dataInicio
    );

}


