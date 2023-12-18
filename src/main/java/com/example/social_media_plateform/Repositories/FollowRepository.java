package com.example.social_media_plateform.Repositories;

import com.example.social_media_plateform.Models.Follow;
import com.example.social_media_plateform.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
