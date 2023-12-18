package com.example.social_media_plateform.Services.Impls;

import com.example.social_media_plateform.DTOs.responseDTOs.UserProfileResponseDTO;
import com.example.social_media_plateform.Exceptions.UserNotFoundException;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.UserRepository;
import com.example.social_media_plateform.Transformers.UserTransformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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


    public UserProfileResponseDTO updateBio(String username, String bio) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setBio(bio);

        User savedUser=userRepository.save(user);

        UserProfileResponseDTO response= UserTransformers.userToUserProfileResponse(user);

        return response;
    }
}
