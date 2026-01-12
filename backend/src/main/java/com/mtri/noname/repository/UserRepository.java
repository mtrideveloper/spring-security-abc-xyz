package com.mtri.noname.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mtri.noname.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
}
