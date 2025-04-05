package com.socialmood.socialmoodapi.controllers;

import com.socialmood.socialmoodapi.dto.AuthenticationDTO;
import com.socialmood.socialmoodapi.dto.LoginResponseDTO;
import com.socialmood.socialmoodapi.dto.RegisterDTO;
import com.socialmood.socialmoodapi.entitys.User;
import com.socialmood.socialmoodapi.repositorys.IUserRepository;
import com.socialmood.socialmoodapi.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository repository;

    @Autowired
    private TokenService tokenService;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());
            if (token != null) {
                User user = (User) repository.findByEmail(data.email());
                if (user != null) {
                    return ResponseEntity.ok(new LoginResponseDTO(token, user.getId(), user.getNome(), user.getEmail(), user.getRole()));
                }
            }
        } catch (Exception e)  {
            System.out.println("Erro:" + e);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível realizar login");
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data){
        try {
            if(this.repository.findByEmail(data.email()) != null) return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe!");

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
            User newUser = new User(data.nome(), data.sobrenome(),
                    data.email(), encryptedPassword, data.telefone(),
                    data.genero(), data.role()
            );

            this.repository.save(newUser);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Erro:" + e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível realizar o cadastro");
    }
}
