package com.socialmood.socialmoodapi.repositorys;

import com.socialmood.socialmoodapi.entitys.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface SessionRepository extends JpaRepository<Session, Long> {

}
