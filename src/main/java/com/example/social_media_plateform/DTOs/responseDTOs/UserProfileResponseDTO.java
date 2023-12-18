package com.example.social_media_plateform.DTOs.responseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePicUrl;
    private LocalDateTime joinedAt;
}
