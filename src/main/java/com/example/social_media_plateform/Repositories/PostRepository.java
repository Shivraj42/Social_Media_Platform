package com.example.social_media_plateform.Repositories;

import com.example.social_media_plateform.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    Optional<Object> findByPostKey(String postId);
}
