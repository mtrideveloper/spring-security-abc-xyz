package com.mtri.noname.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtri.noname.dto.res.ApiRes;
import com.mtri.noname.model.Video;
import com.mtri.noname.service.MuxVideoService;
import com.mtri.noname.service.VideoService;
import com.mux.ApiException;
import com.mux.sdk.models.Upload;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/studio")
public class VideoController {
    private final VideoService videoService;
    private final MuxVideoService muxVideoService;

    public VideoController(VideoService videoService, MuxVideoService muxVideoService) {
        this.videoService = videoService;
        this.muxVideoService = muxVideoService;
    }

    @PostMapping("/upload")
    public ApiRes<Map<String, String>> createUpload() {
        ApiRes<Map<String, String>> res = new ApiRes<>();
        
        try {
            Upload upload = muxVideoService.createUploadUrl();

            // Trả về đúng format mà Frontend cần (url và id)
            Map<String, String> response = new HashMap<>();
            response.put("url", upload.getUrl()); // Đây là endpoint để đưa vào <MuxUploader>
            response.put("id", upload.getId()); // ID của phiên upload

            // Lưu ý: Lúc này chưa có asset_id, asset_id chỉ có khi upload xong

            // return ResponseEntity.ok(response);

            res.setCode(HttpStatus.OK.value());
            res.setMessage("Success");
            res.setResult(response);

            return res;

        } catch (ApiException e) {
            res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setMessage("Exception: " + e.getMessage());

            return res;
        }
    }

    @GetMapping("/videos")
    public ApiRes<PagedModel<Video>> getAllVideos(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        PagedModel<Video> videos = videoService.findAllVideos(page, size);
        ApiRes<PagedModel<Video>> res = new ApiRes<>();

        res.setCode(HttpStatus.OK.value());
        res.setMessage("Success");
        res.setResult(videos);

        return res;
    }

}
