package com.example.social_media_plateform.Services.Impls;

import com.example.social_media_plateform.Exceptions.PostNotFoundException;
import com.example.social_media_plateform.Exceptions.UserNotFoundException;
import com.example.social_media_plateform.Models.Comment;
import com.example.social_media_plateform.Models.Post;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.CommentRepository;
import com.example.social_media_plateform.Repositories.PostRepository;
import com.example.social_media_plateform.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    @Autowired
    public CommentService(UserRepository userRepository,
                          PostRepository postRepository,
                          CommentRepository commentRepository,
                          NotificationService notificationService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
    }


    public String addComment(String username, String postId, String content) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = (Post)postRepository.findByPostKey(postId)
                .orElseThrow(()-> new PostNotFoundException("Post Not Found!"));

        Comment comment= Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

        Comment savedComment= commentRepository.save(comment);

        notificationService.sendCommentNotification(comment);

        return  "Comment added successfully!";
    }


    public String likePost(String username, String postId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = (Post)postRepository.findByPostKey(postId)
                .orElseThrow(()-> new PostNotFoundException("Post Not Found!"));

        post.getLikedByUsers().add(user);
        postRepository.save(post);

        notificationService.sendLikedNotification(user, post);

        return "Post liked Successfully";
    }
}
