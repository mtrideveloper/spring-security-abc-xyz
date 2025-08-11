package com.mtri.auths.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mtri.auths.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
}
