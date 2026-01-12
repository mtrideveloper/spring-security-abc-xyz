package com.mtri.noname.model;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "playlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Playlist {
    @Id
    private String id;

    private String name;
    private String description;

    private String userId; // Chủ sở hữu playlist

    // Lưu danh sách Video ID trực tiếp trong mảng
    @Builder.Default // Khởi tạo mặc định để tránh NullPointerException
    private List<String> videoIds = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}