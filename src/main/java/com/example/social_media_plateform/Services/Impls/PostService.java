package com.example.social_media_plateform.Services.Impls;

import com.example.social_media_plateform.DTOs.requestDTOs.PostRequestDTO;
import com.example.social_media_plateform.DTOs.responseDTOs.PostResponseDTO;
import com.example.social_media_plateform.Enums.FriendshipStatus;
import com.example.social_media_plateform.Enums.PrivacySetting;
import com.example.social_media_plateform.Exceptions.PostNotFoundException;
import com.example.social_media_plateform.Exceptions.PrivateContentException;
import com.example.social_media_plateform.Exceptions.UserNotFoundException;
import com.example.social_media_plateform.Models.Follow;
import com.example.social_media_plateform.Models.Friendship;
import com.example.social_media_plateform.Models.Post;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.FriendshipRepository;
import com.example.social_media_plateform.Repositories.PostRepository;
import com.example.social_media_plateform.Repositories.UserRepository;
import com.example.social_media_plateform.Transformers.PostTransformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       S3Service s3Service,
                       FriendshipRepository friendshipRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.s3Service = s3Service;
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * Creates a new post for a user.
     *
     * @param username         The username of the user creating the post.
     * @param postRequestDTO   The data for the new post.
     * @param file             The media file associated with the post.
     * @return The response DTO for the created post.
     * @throws IOException If there is an issue with the media file.
     */
    public PostResponseDTO createPost(String username, PostRequestDTO postRequestDTO, MultipartFile file) throws IOException {
        // Retrieve the user from the database using the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Upload the media file to Amazon S3 and get the URL
        String mediaFileUrl = s3Service.uploadFile(file);

        // Transform the DTO and create a new Post entity
        Post post = PostTransformers.postRequestDTOToPost(mediaFileUrl, user, postRequestDTO);

        // Save the post to the database
        Post savedPost = postRepository.save(post);

        // Transform the saved post to a response DTO
        PostResponseDTO response = PostTransformers.postToPostResponseDTO(savedPost);
        return response;
    }

    /**
     * Reposts a public post from another user.
     *
     * @param username        The username of the user reposting the post.
     * @param postId          The ID of the post being reposted.
     * @param privacySetting  The privacy setting for the reposted post.
     * @return The response DTO for the reposted post.
     */
    public PostResponseDTO repostPost(String username, String postId, PrivacySetting privacySetting) {
        // Retrieve the user from the database using the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Retrieve the original post from the database using the post ID
        Post post = postRepository.findByPostKey(postId)
                .orElseThrow(() -> new PostNotFoundException("Post Not Found!"));

        // Check if the original post is public
        if (post.getPrivacySetting() == PrivacySetting.PUBLIC) {
            // Create a new Post entity for the repost
            Post repost = Post.builder()
                    .postKey(UUID.randomUUID().toString())
                    .privacySetting(privacySetting)
                    .content(post.getContent())
                    .mediaURL(post.getMediaURL())
                    .user(user)
                    .reposted(true)
                    .build();

            // Save the repost to the database
            Post savedRepost = postRepository.save(repost);

            // Transform the saved repost to a response DTO
            PostResponseDTO response = PostTransformers.postToPostResponseDTO(savedRepost);
            return response;
        } else {
            throw new PrivateContentException("Private post, Cannot repost it!");
        }
    }

    /**
     * Views all posts from a specified user, considering privacy settings.
     *
     * @param myUsername      The username of the viewing user.
     * @param targetUsername  The username of the user whose posts are being viewed.
     * @return A list of response DTOs for the posts.
     */
    public List<PostResponseDTO> viewAllPosts(String myUsername, String targetUsername) {
        // Retrieve the viewing user from the database using the username
        User myUser = userRepository.findByUsername(myUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Retrieve the target user from the database using the username
        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if there is an existing friendship between the users
        Optional<Friendship> friendship = friendshipRepository.findFriendshipsByUsers(myUser, targetUser);

        // If there is a friendship, view all posts otherwise, view public posts only
        if (friendship.isPresent()) {
            List<PostResponseDTO> response = new ArrayList<>();
            for (Post post : targetUser.getPosts()) {
                response.add(PostTransformers.postToPostResponseDTO(post));
            }
            return response;
        } else {
            List<PostResponseDTO> response = new ArrayList<>();
            for (Post post : targetUser.getPosts()) {
                if (post.getPrivacySetting() == PrivacySetting.PUBLIC) {
                    response.add(PostTransformers.postToPostResponseDTO(post));
                }
            }
            return response;
        }
    }

    /**
     * Generates a personalized feed of posts for a user based on followed users and friends.
     *
     * @param username The username of the user generating the feed.
     * @return A list of response DTOs for the posts in the feed.
     */
    public List<PostResponseDTO> generateFeed(String username) {
        // Retrieve the current user from the database using the username
        User currUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Set to store unique response DTOs for the feed
        Set<PostResponseDTO> response = new HashSet<>();

        // Retrieve the users being followed by the current user
        Set<Follow> followings = currUser.getFollowing();

        // Iterate over followings to get posts and add them to the response set
        for (Follow follow : followings) {
            List<Post> posts = postRepository.findByUserAndPrivacySetting(follow.getFollowing(), PrivacySetting.PUBLIC);
            for (Post post : posts) {
                response.add(PostTransformers.postToPostResponseDTO(post));
            }
        }

        // Retrieve friendships of the current user
        List<Friendship> friendships = friendshipRepository.findFriendshipsByUser(currUser);

        // Iterate over friendships to get posts and add them to the response set
        for (Friendship friendship : friendships) {
            User user = (friendship.getUser1().equals(currUser)) ? friendship.getUser2() : friendship.getUser1();
            List<Post> posts = postRepository.findByUser(user);
            for (Post post : posts) {
                response.add(PostTransformers.postToPostResponseDTO(post));
            }
        }

        // Convert the set to a list for the final response
        List<PostResponseDTO> responseDTOList = response.stream().toList();
        return responseDTOList;
    }
}
