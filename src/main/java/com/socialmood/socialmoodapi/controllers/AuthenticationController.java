package com.socialmood.socialmoodapi.controllers;

import com.socialmood.socialmoodapi.dto.AuthenticationDTO;
import com.socialmood.socialmoodapi.dto.LoginResponseDTO;
import com.socialmood.socialmoodapi.dto.RegisterDTO;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.repositorys.UserRepository;
import com.socialmood.socialmoodapi.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (Exception e)  {
            System.out.println("Erro:" + e);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível realizar login");
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data){
        if(this.repository.findByEmail(data.email()) != null) return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe!");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        User newUser = new User(data.nome(), data.sobrenome(),
                data.email(), encryptedPassword, data.telefone(),
                data.genero(), data.role()
        );

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
