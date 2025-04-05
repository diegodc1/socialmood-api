package com.socialmood.socialmoodapi.repositorys;

import com.socialmood.socialmoodapi.entitys.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISessionRepository extends JpaRepository<Session, Long> {

}
