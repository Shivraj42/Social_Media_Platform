package com.example.social_media_plateform.Services.Impls;

import com.example.social_media_plateform.DTOs.responseDTOs.PostResponseDTO;
import com.example.social_media_plateform.DTOs.responseDTOs.UserProfileResponseDTO;
import com.example.social_media_plateform.Exceptions.UserNotFoundException;
import com.example.social_media_plateform.Models.Post;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.UserRepository;
import com.example.social_media_plateform.Transformers.PostTransformers;
import com.example.social_media_plateform.Transformers.UserTransformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    final UserRepository userRepository;

    private final S3Service s3Service;
    @Autowired
    public UserService(UserRepository userRepository,
                       S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    /**
     * Custom implementation of the UserDetailsService interface for loading user details by username or email.
     *
     * @return An instance of UserDetailsService.
     */
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {

            /**
             * Load user details by username or email.
             *
             * @param usernameOrEmail The username or email used for authentication.
             * @return UserDetails containing user information.
             * @throws UsernameNotFoundException if the username or email is not found.
             */
            @Override
            public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
                User user= userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                        .orElseThrow(()-> new UsernameNotFoundException("Username or email not found!"));
                return user;
            }
        };
    }

    /**
     * Save a user to the repository. If the user has no ID, set the creation timestamp.
     * Always set the update timestamp before saving.
     *
     * @param user The user to be saved.
     * @return The saved user with updated timestamps.
     */
    public User save(User user){
        if(user.getId() == null){
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }


    /**
     * Updates the user's bio.
     *
     * @param username The username of the user.
     * @param bio      The new bio to be set.
     * @return A UserProfileResponseDTO containing the updated user profile information.
     */
    public UserProfileResponseDTO updateBio(String username, String bio) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setBio(bio);

        User savedUser = userRepository.save(user);

        UserProfileResponseDTO response = UserTransformers.userToUserProfileResponse(savedUser);

        return response;
    }

    /**
     * Updates the user's profile picture.
     *
     * @param username The username of the user.
     * @param file     The new profile picture file.
     * @return A UserProfileResponseDTO containing the updated user profile information.
     * @throws IOException If there is an issue with file handling.
     */
    public UserProfileResponseDTO updateProfilePic(String username, MultipartFile file) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String profilePicUrl = s3Service.uploadFile(file);

        user.setProfilePicUrl(profilePicUrl);

        User savedUser = userRepository.save(user);

        UserProfileResponseDTO response = UserTransformers.userToUserProfileResponse(savedUser);

        return response;
    }

    /**
     * Disables a user.
     *
     * @param username The username of the user to be disabled.
     * @return A success message indicating that the user was disabled successfully.
     */
    public String disableUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setDisabled(true);
        userRepository.save(user);
        return "User disabled successfully!";
    }

    /**
     * Enables a user.
     *
     * @param username The username of the user to be enabled.
     * @return A success message indicating that the user was enabled successfully.
     */
    public String enableUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setDisabled(false);
        userRepository.save(user);
        return "User enabled successfully!";
    }

    /**
     * Views the user's profile.
     *
     * @param username The username of the user whose profile is to be viewed.
     * @return A UserProfileResponseDTO containing the user's profile information.
     */
    public UserProfileResponseDTO viewProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserProfileResponseDTO userProfileResponseDTO = UserTransformers.userToUserProfileResponse(user);
        return userProfileResponseDTO;
    }

    /**
     * Views all posts of a user.
     *
     * @param username The username of the user whose posts are to be viewed.
     * @return A list of PostResponseDTOs containing information about each post.
     */
    public List<PostResponseDTO> viewAllPost(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<PostResponseDTO> response = new ArrayList<>();
        List<Post> posts = user.getPosts();
        for (Post post : posts) {
            PostResponseDTO postResponseDTO = PostTransformers.postToPostResponseDTO(post);
            response.add(postResponseDTO);
        }
        return response;
    }
}
