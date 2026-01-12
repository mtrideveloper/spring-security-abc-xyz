package com.mtri.noname.model;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "subscriptions")
// ???
@CompoundIndex(name = "viewer_creator_idx", def = "{'viewerId': 1, 'creatorId': 1}", unique = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    private String id;

    private String viewerId; // Người đăng ký
    private String creatorId; // Kênh được đăng ký

    @CreatedDate
    private Instant createdAt;
}