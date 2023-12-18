package com.example.social_media_plateform.Repositories;

import com.example.social_media_plateform.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailOrUsername(String email, String username);

    Optional<User> findByUsername(String follower);
}
