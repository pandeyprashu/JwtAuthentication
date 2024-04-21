package com.example.JwtAuthentication.authdata;

public record AuthResponseDto(String token, AuthStatus authStatus) {
}
