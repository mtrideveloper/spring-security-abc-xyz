package com.mtri.noname.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import com.mtri.noname.model.Video;
import com.mtri.noname.repository.VideoRepository;

@Service
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository repository) {
        this.videoRepository = repository;
    }

    public PagedModel<Video> findAllVideos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoRepository.findAllByOrderByCreatedAtDesc(pageable);

        return new PagedModel<>(videos);
    }

    public Video findVideoById(String id) {
        return videoRepository.findById(id).orElse(null);
    }

    public List<Video> findVideosByUserId(String userId) {
        return videoRepository.findByUserId(userId);
    }

    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }
}
