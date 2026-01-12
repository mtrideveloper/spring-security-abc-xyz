package com.mtri.noname.model;

import com.mtri.noname.enums.ReactionType;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "video_reactions")
@CompoundIndex(name = "user_video_reaction_idx", def = "{'userId': 1, 'videoId': 1}", unique = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoReaction {
    @Id
    private String id;
    private String userId;
    private String videoId;
    /**
     * LIKE / DISLIKE
     */
    private ReactionType type;

    @CreatedDate
    private Instant createdAt;
}