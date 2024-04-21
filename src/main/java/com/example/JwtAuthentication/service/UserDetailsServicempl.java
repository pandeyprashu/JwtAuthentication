package com.example.JwtAuthentication.service;

import com.example.JwtAuthentication.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServicempl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         var user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));

         return  new User(user.getUsername(),user.getPassword(),user.getAuthorities());
    }
}
