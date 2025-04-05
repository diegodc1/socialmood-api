package com.socialmood.socialmoodapi.repositorys;

import com.socialmood.socialmoodapi.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);
}
