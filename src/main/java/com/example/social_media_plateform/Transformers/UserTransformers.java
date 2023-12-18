package com.example.social_media_plateform.Transformers;

import com.example.social_media_plateform.DTOs.responseDTOs.UserProfileResponseDTO;
import com.example.social_media_plateform.Models.User;

public class UserTransformers {

    public static UserProfileResponseDTO userToUserProfileResponse(User user){

        UserProfileResponseDTO userProfileResponseDTO= UserProfileResponseDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePicUrl(user.getProfilePicUrl())
                .bio(user.getBio())
                .joinedAt(user.getCreatedAt())
                .build();

        return userProfileResponseDTO;
    }
}
