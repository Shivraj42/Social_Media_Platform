package com.example.social_media_plateform.DTOs.requestDTOs;

import com.example.social_media_plateform.Enums.PrivacySetting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {

    private String content;
    private PrivacySetting privacySetting;
}
