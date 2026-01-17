package com.mtri.noname.service;

import com.mtri.noname.enums.VideoVisibility;
import com.mtri.noname.model.User;
import com.mtri.noname.model.Video;
import com.mtri.noname.service.auth.UserService;
import com.mux.ApiClient;
import com.mux.ApiException;
import com.mux.sdk.DirectUploadsApi;
import com.mux.sdk.models.CreateUploadRequest;
import com.mux.sdk.models.CreateAssetRequest;
import com.mux.sdk.models.PlaybackPolicy;
import com.mux.sdk.models.Upload;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MuxVideoService {

    private final DirectUploadsApi directUploadsApi;
    private final VideoService videoService;
    private final UserService userService;

    // Spring tự động Inject ApiClient đã cấu hình ở bước 1 vào đây
    public MuxVideoService(ApiClient apiClient, VideoService videoService, UserService userService) {
        this.directUploadsApi = new DirectUploadsApi(apiClient);
        this.videoService = videoService;
        this.userService = userService;
    }

    public Upload createUploadUrl(String userEmail) throws ApiException {
        CreateUploadRequest request = new CreateUploadRequest();
        
        // 1. Cấu hình cho video sau khi upload xong (Asset)
        CreateAssetRequest newAssetSettings = new CreateAssetRequest();
        newAssetSettings.setPlaybackPolicy(List.of(PlaybackPolicy.PUBLIC)); 
        // Nếu muốn video riêng tư thì xóa dòng trên hoặc set SIGNED
        
        request.setNewAssetSettings(newAssetSettings);

        // 2. QUAN TRỌNG: Cho phép upload trực tiếp từ trình duyệt (CORS của Mux)
        // Nếu không có dòng này, React upload sẽ bị lỗi chặn CORS
        request.setCorsOrigin("*"); 

        // 3. Gọi API tạo link
        Upload uploadData = directUploadsApi.createDirectUpload(request).execute().getData();

        if (uploadData == null)
            throw new ApiException("Failed to create upload URL");
        
        Video undifinedVideo = new Video();

        User user = userService.getUserByEmail(userEmail);
        
        if (user == null) {
            throw new ApiException("User not found with email: " + userEmail);
        }
        undifinedVideo.setMuxUploadId(uploadData.getId());
        undifinedVideo.setMuxAssetId(uploadData.getAssetId());
        undifinedVideo.setMuxStatus("waiting");
        undifinedVideo.setUserId(user.getId());
        
        //#region Set default fields
        undifinedVideo.setTitle("Untitled Video");
        undifinedVideo.setDescription("");
        undifinedVideo.setDuration(3);
        undifinedVideo.setVisibility(VideoVisibility.PUBLIC);
        undifinedVideo.setCreatedAt(Instant.now());
        //#endregion

        videoService.saveVideo(undifinedVideo);

        return uploadData;
    }
}