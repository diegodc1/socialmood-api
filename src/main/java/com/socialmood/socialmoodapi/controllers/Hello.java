package com.socialmood.socialmoodapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/hello")
public class Hello {

    @GetMapping()
    public ResponseEntity<?> helloWordl() {
        return ResponseEntity.ok().body("Olá!");
    }
}
