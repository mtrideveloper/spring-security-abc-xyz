package com.mtri.noname.model;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "video_views")
// Tùy logic: Nếu muốn 1 user xem nhiều lần vẫn tính thì bỏ unique=true đi
@CompoundIndex(name = "user_video_view_idx", def = "{'userId': 1, 'videoId': 1}", unique = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoView {
    @Id
    private String id;
    private String userId;
    private String videoId;

    @CreatedDate
    private Instant createdAt;
}