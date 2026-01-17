package com.mtri.noname.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.mtri.noname.model.Video;
import java.util.List;


public interface VideoRepository extends MongoRepository<Video, String> {
    Page<Video> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Optional<Video> findById(String id);
    List<Video> findByUserId(String userId);
}
