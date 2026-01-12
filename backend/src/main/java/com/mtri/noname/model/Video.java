package com.mtri.noname.model;

import com.mtri.noname.enums.VideoVisibility;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {
	@Id
	private String id;

	private String title;
	private String description;

	// --- Mux Data ---
	private String muxStatus;

	@Indexed(unique = true, sparse = true)
	private String muxAssetId;

	@Indexed(unique = true, sparse = true)
	private String muxUploadId;

	@Indexed(unique = true, sparse = true)
	private String muxPlaybackId;

	// --- Media Info ---
	private String thumbnailUrl;
	private String thumbnailKey;
	private String previewUrl;
	private String previewKey;

	@Builder.Default
	private Integer duration = 0; // Tính bằng giây

	@Builder.Default
	private VideoVisibility visibility = VideoVisibility.PRIVATE;

	// --- Relations (Lưu ID dạng String) ---
	@Indexed
	private String userId; // Người đăng

	@Indexed
	private String categoryId; // Danh mục

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;
}