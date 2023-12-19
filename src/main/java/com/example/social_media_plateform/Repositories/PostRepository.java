package com.example.social_media_plateform.Repositories;

import com.example.social_media_plateform.Enums.PrivacySetting;
import com.example.social_media_plateform.Models.Post;
import com.example.social_media_plateform.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    Optional<Post> findByPostKey(String postId);


    List<Post> findByUserAndPrivacySetting(User currUser, PrivacySetting privacySetting);

    List<Post> findByUser(User user);
}
