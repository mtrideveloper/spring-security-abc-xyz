package com.mtri.noname.service;

import com.mux.ApiClient;
import com.mux.ApiException;
import com.mux.sdk.DirectUploadsApi;
import com.mux.sdk.models.CreateUploadRequest;
import com.mux.sdk.models.CreateAssetRequest;
import com.mux.sdk.models.PlaybackPolicy;
import com.mux.sdk.models.Upload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MuxVideoService {

    private final DirectUploadsApi directUploadsApi;

    // Spring tự động Inject ApiClient đã cấu hình ở bước 1 vào đây
    public MuxVideoService(ApiClient apiClient) {
        this.directUploadsApi = new DirectUploadsApi(apiClient);
    }

    public Upload createUploadUrl() throws ApiException {
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
        return directUploadsApi.createDirectUpload(request).execute().getData();
    }
}