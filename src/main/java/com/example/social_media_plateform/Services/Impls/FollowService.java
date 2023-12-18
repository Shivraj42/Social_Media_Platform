package com.example.social_media_plateform.Services.Impls;

import com.example.social_media_plateform.Exceptions.UserNotFoundException;
import com.example.social_media_plateform.Models.Follow;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.FollowRepository;
import com.example.social_media_plateform.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository,
                         UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }



    public String followUser(String follower, String following) {

        User followerUser =userRepository.findByUsername(follower).orElseThrow(() -> new UserNotFoundException("User Not Found!"));

        User followingUser=userRepository.findByUsername(following).orElseThrow(() -> new UserNotFoundException("User Not Found!"));


        if (!isFollowing(followerUser, followingUser)) {

            Follow follow = Follow.builder()
                    .following(followingUser)
                    .followDate(LocalDateTime.now())
                    .follower(followerUser)
                    .build();

            followerUser.getFollowing().add(follow);
           userRepository.save(followerUser);
           followRepository.save(follow);

            return "Followed "+ following+ " Successfully!";
        }
        else return"Already Following!";

    }

    private boolean isFollowing(User follower, User following) {
        Optional<Follow> follow= followRepository.findByFollowerAndFollowing(follower, following);
        if(follow.isPresent()) return true;
        else return false;
    }


    public String unfollowUser(String follower, String followed) {

        User followerUser =userRepository.findByUsername(follower).orElseThrow(() -> new UserNotFoundException("User Not Found!"));

        User followedUser=userRepository.findByUsername(followed).orElseThrow(() -> new UserNotFoundException("User Not Found!"));

        Optional<Follow> optionalFollow= followRepository.findByFollowerAndFollowing(followerUser, followedUser);
        if(optionalFollow.isPresent()){
            followerUser.getFollowing().remove(followedUser);
            followedUser.getFollowers().remove(followerUser);
            followRepository.deleteById(optionalFollow.get().getFollowID());
            userRepository.save(followerUser);
            userRepository.save(followedUser);
            return "Unfollowed Successfully!";
        }
        return "Already Unfollowed!";
    }
}
