package com.socialmood.socialmoodapi.services;

import com.socialmood.socialmoodapi.dto.UserDetailsResponseDTO;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.repositorys.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private IUserRepository IUserRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User findUserById(Long userId) {
        return IUserRepository.findById(userId).orElse(null);
    }

    public UserDetailsResponseDTO findUserDetailsById(Long userId) {
        User user = IUserRepository.findById(userId).orElse(null);

        if (user != null ) {
            return new UserDetailsResponseDTO(
                    user.getId(),
                    user.getNome(),
                    user.getSobrenome(),
                    user.getEmail(),
                    user.getRole(),
                    user.getFotoPerfil(),
                    user.getTelefone(),
                    user.getGenero()
            );
        } else {
            return null;
        }
    }

    public Boolean updateUserEmail(Long userId, String email) {
        try {
            User user = IUserRepository.findById(userId).orElse(null);

            if (user != null) {
                user.setEmail(email);
                IUserRepository.save(user);
                return true;
            }
            log.error("Não foi possível atualizar o email pois o usuário não foi encontrado!");
        } catch (Exception e) {
            log.error("Ocorreu um erro ao atualizar o email do usuário: " + userId, e);
        }
        return false;
    }

    public Boolean updateUserPhone(Long userId, String phone) {
        try {
            User user = IUserRepository.findById(userId).orElse(null);

            if (user != null) {
                user.setTelefone(phone);
                IUserRepository.save(user);
                return true;
            }
            log.error("Não foi possível atualizar o email pois o usuário não foi encontrado!");
        } catch (Exception e) {
            log.error("Ocorreu um erro ao atualizar o email do usuário: " + userId, e);
        }
        return false;
    }
}
