package com.example.JwtAuthentication.controller;


import com.example.JwtAuthentication.authdata.AuthRequestDto;
import com.example.JwtAuthentication.authdata.AuthResponseDto;
import com.example.JwtAuthentication.authdata.AuthStatus;
import com.example.JwtAuthentication.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());
        new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESSFULLY);
        return ResponseEntity.ok(new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESSFULLY));

    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody AuthRequestDto authRequestDto) {
        try {
            var jwtToken = authService.signup(authRequestDto.name(), authRequestDto.username(), authRequestDto.password());
            return ResponseEntity.ok(new AuthResponseDto(jwtToken, AuthStatus.USER_CREATED_SUCCESSFULLY));

        } catch (Exception e) {
            authService.signup(authRequestDto.name(), authRequestDto.username(), authRequestDto.password());
            var authResponse = new AuthResponseDto(null, AuthStatus.USER_NOT_CREATED);
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(authResponse);
        }

    }
}
