package com.example.social_media_plateform.Repositories;

import com.example.social_media_plateform.Models.Friendship;
import com.example.social_media_plateform.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

   public Optional<Friendship> findByUser1AndUser2(User sender, User receiver);
}
