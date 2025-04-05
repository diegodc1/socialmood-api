package com.socialmood.socialmoodapi.services;

import com.socialmood.socialmoodapi.dto.UserDetailsResponseDTO;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.repositorys.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
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
}
