package com.example.social_media_plateform.Repositories;

import com.example.social_media_plateform.Enums.FriendshipStatus;
import com.example.social_media_plateform.Models.Friendship;
import com.example.social_media_plateform.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

   public Optional<Friendship> findByUser1AndUser2(User sender, User receiver);

   @Query(value = "SELECT f FROM Friendship f WHERE ((f.user1 = :user1 AND f.user2 = :user2) OR (f.user1 = :user2 AND f.user2 = :user1)) AND f.status = 'ACCEPTED'")
   public Optional<Friendship> findFriendshipsByUsers(User user1, User user2);


   @Query(value = "SELECT f FROM Friendship f WHERE ((f.user1 = :user) OR (f.user2 = :user)) AND f.status = 'ACCEPTED'")
   public List<Friendship> findFriendshipsByUser(User user);
}
