package com.example.social_media_plateform.Services.Impls;

import com.example.social_media_plateform.DTOs.requestDTOs.PostRequestDTO;
import com.example.social_media_plateform.Enums.PrivacySetting;
import com.example.social_media_plateform.Exceptions.PostNotFoundException;
import com.example.social_media_plateform.Exceptions.UserNotFoundException;
import com.example.social_media_plateform.Models.Post;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.PostRepository;
import com.example.social_media_plateform.Repositories.UserRepository;
import com.example.social_media_plateform.Transformers.PostTransformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       S3Service s3Service) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }


    public String createPost(PostRequestDTO postRequestDTO, MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(postRequestDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String mediaFileUrl= s3Service.uploadFile(file);

        Post post= PostTransformers.postRequestDTOToPost(mediaFileUrl, user, postRequestDTO);

        Post savedPost= postRepository.save(post);

        return "Post created successfully!";
    }


    public String repostPost(String username, String postId, PrivacySetting privacySetting) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = (Post)postRepository.findByPostKey(postId)
                .orElseThrow(()-> new PostNotFoundException("Post Not Found!"));

        if(post.getPrivacySetting()==PrivacySetting.PUBLIC){

            Post repost= Post.builder()
                    .postKey(UUID.randomUUID().toString())
                    .privacySetting(privacySetting)
                    .content(post.getContent())
                    .mediaURL(post.getMediaURL())
                    .user(user)
                    .reposted(true)
                    .build();

            Post savedRepost= postRepository.save(repost);
            return "Post reposted successfully!";
        }
        else return "Cannot repost private post!";
    }
}
