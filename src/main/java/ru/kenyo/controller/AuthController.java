package ru.kenyo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.kenyo.dto.LoginDTO;
import ru.kenyo.dto.WebTokenDTO;
import ru.kenyo.service.AuthService;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        return ResponseEntity.ok(new WebTokenDTO(authService.login(login)));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        return ResponseEntity.ok().build();
    }
}
