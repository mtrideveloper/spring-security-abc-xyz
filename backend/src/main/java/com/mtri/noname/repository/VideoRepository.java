package com.mtri.noname.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.mtri.noname.model.Video;

public interface VideoRepository extends MongoRepository<Video, String> {
    Page<Video> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
