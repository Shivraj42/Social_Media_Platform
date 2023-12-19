package com.example.social_media_plateform.DTOs.responseDTOs;

import com.example.social_media_plateform.Enums.PrivacySetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDTO {
    private Long postID;
    private String username;
    private String content;
    private String mediaURL;
    private LocalDateTime postDate;
    private PrivacySetting privacySetting;
    private int likesCount;
    private int commentsCount;
    private boolean isReposted;
}
