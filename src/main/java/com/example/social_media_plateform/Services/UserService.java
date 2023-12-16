package com.example.social_media_plateform.Services;

import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
                User user= userRepository.findByEmailOrUsername(usernameOrEmail, usernameOrEmail)
                        .orElseThrow(()-> new UsernameNotFoundException("Username or email not found!"));
                return user;
            }
        };
    }

    public User save(User user){
        if(user.getId() == null){
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }


}
