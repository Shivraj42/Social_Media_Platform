package com.example.social_media_plateform.Transformers;

import com.example.social_media_plateform.DTOs.requestDTOs.PostRequestDTO;
import com.example.social_media_plateform.DTOs.responseDTOs.PostResponseDTO;
import com.example.social_media_plateform.Models.Post;
import com.example.social_media_plateform.Models.User;

import java.util.UUID;

public class PostTransformers {
    public static Post postRequestDTOToPost(String mediaUrl, User user, PostRequestDTO postRequestDTO){
        Post post= Post.builder()
                .postKey(UUID.randomUUID().toString())
                .content(postRequestDTO.getContent())
                .user(user)
                .mediaURL(mediaUrl)
                .privacySetting(postRequestDTO.getPrivacySetting())
                .build();
        return post;
    }
    public static PostResponseDTO postToPostResponseDTO(Post post){

        PostResponseDTO postResponseDTO =PostResponseDTO.builder()
                .postDate(post.getPostDate())
                .postID(post.getPostID())
                .commentsCount(post.getComments().size())
                .username(post.getUser().getUsername())
                .likesCount(post.getLikedByUsers().size())
                .isReposted(post.isReposted())
                .content(post.getContent())
                .mediaURL(post.getMediaURL())
                .privacySetting(post.getPrivacySetting())
                .build();

        return postResponseDTO;
    }
}
