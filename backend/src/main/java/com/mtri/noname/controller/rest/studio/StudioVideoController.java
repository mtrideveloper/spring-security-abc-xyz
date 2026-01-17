package com.mtri.noname.controller.rest.studio;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtri.noname.dto.res.ApiRes;
import com.mtri.noname.model.Video;
import com.mtri.noname.service.MuxVideoService;
import com.mtri.noname.service.VideoService;
import com.mtri.noname.util.SecurityChecker;
import com.mux.ApiException;
import com.mux.sdk.models.Upload;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/studio")
public class StudioVideoController {
    private final VideoService videoService;
    private final MuxVideoService muxVideoService;

    public StudioVideoController(VideoService videoService, MuxVideoService muxVideoService) {
        this.videoService = videoService;
        this.muxVideoService = muxVideoService;
    }

    @PostMapping("/upload")
    public ApiRes<Map<String, String>> createUpload(Principal principal) {
        ApiRes<Map<String, String>> res = new ApiRes<>();

        if (!SecurityChecker.isValidPrincipal(principal)) {
            res.setCode(HttpStatus.UNAUTHORIZED.value());
            res.setMessage("Unauthorized");
            return res;
        }

        String email = SecurityChecker.extractEmailFromPrincipal(principal);

        if (email == null) {
            res.setCode(HttpStatus.BAD_REQUEST.value());
            res.setMessage("Email does not match.");
            return res;
        }

        try {
            Upload upload = muxVideoService.createUploadUrl(email);

            // Lưu ý: Lúc này chưa có asset_id, asset_id chỉ có khi upload xong
            // Trả về đúng format mà Frontend cần (url và id)
            Map<String, String> response = new HashMap<>();
            response.put("url", upload.getUrl()); // Đây là endpoint để đưa vào <MuxUploader>
            response.put("id", upload.getId()); // ID của phiên upload

            System.out.println("upload id: " + upload.getId());
            System.out.println("upload url: " + upload.getUrl());

            res.setCode(HttpStatus.OK.value());
            res.setMessage("Success");
            res.setResult(response);

        } catch (ApiException e) {
            res.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setMessage("Exception: " + e.getMessage());
        }

        return res;
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

    /**
     * The code are generated to test mux webhook api.
     * Webhook URL in dashboard: https://unoccidental-emmitt-determinedly.ngrok-free.dev/studio/testwebhook
     * @param payload
     * @return
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleMuxWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Webhook nhận được: " + payload);

        // Kiểm tra loại sự kiện
        String type = (String) payload.get("type");

        if ("video.asset.ready".equals(type)) {
            Object dataObject = payload.get("data");

            if (!(dataObject instanceof Map))
                return ResponseEntity.badRequest().body("Data invalid!");

            Map<String, Object> dataMap = (Map<String, Object>) dataObject;

            String assetId = (String) dataMap.get("id");
            System.out.println("Video đã sẵn sàng với assetId: " + assetId);

            // TODO: Lưu assetId vào database hoặc cập nhật trạng thái video
        }

        return ResponseEntity.ok("Webhook received");
    }
}
