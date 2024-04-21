package com.example.JwtAuthentication.service;

import com.example.JwtAuthentication.model.UserModel;
import com.example.JwtAuthentication.repo.UserRepository;
import com.example.JwtAuthentication.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public String login(String username, String password) {
        var authToken = new UsernamePasswordAuthenticationToken(username, password);

        var auth = authenticationManager.authenticate(authToken);


        return JwtUtils.generateToken(((UserDetails) (auth.getPrincipal())).getUsername());

    }

    @Override
    public String signup(String name, String username, String password) {
        //check whether user already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("User already exists");
        }
        //Encode Password
        var encodePassword = passwordEncoder.encode(password);

        // Authorities

        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));


        //Create User
        var user = UserModel.builder()
                .name(name)
                .username(username)
                .password(encodePassword)
                .authorities(authorities)
                .build();

        //Save User
        userRepository.save(user);

        // Generate Token
        return JwtUtils.generateToken(username);

        //return "User created successfully";
    }
}
