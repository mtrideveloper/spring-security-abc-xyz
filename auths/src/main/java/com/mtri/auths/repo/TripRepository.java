package com.mtri.auths.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mtri.auths.model.Trip;

@Repository
public interface TripRepository extends MongoRepository<Trip, String> {
    // List<Trip> findByUserId(String userId);
}
