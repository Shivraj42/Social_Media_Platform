package com.example.social_media_plateform.Repositories;

import com.example.social_media_plateform.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
