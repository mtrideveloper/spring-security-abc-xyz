package com.mtri.auths.service.auth;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mtri.auths.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}