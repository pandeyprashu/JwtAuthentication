package com.example.JwtAuthentication.service;

import org.springframework.stereotype.Service;


public interface AuthService {

    String login(String username, String password);
    String signup(String name,String username, String password);

}
