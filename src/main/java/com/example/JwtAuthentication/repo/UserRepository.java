package com.example.JwtAuthentication.repo;

import com.example.JwtAuthentication.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel,String> {

    boolean existsByUsername(String username);

    Optional<UserModel> findByUsername(String username);
}
