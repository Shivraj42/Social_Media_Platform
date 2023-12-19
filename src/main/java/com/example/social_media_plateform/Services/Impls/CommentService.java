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

    /**
     * Adds a comment to a specified post.
     *
     * @param username The username of the user adding the comment.
     * @param postId   The ID of the post to which the comment is added.
     * @param content  The content of the comment.
     * @return A success message indicating that the comment was added successfully.
     */
    public String addComment(String username, String postId, String content) {
        // Retrieve the user from the database using the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Retrieve the post from the database using the post ID
        Post post = postRepository.findByPostKey(postId)
                .orElseThrow(() -> new PostNotFoundException("Post Not Found!"));

        // Create a new Comment entity
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();

        // Save the comment to the database
        Comment savedComment = commentRepository.save(comment);

        // Send a notification for the added comment
        notificationService.sendCommentNotification(comment);

        return "Comment added successfully!";
    }

    /**
     * Likes a specified post.
     *
     * @param username The username of the user liking the post.
     * @param postId   The ID of the post to be liked.
     * @return A success message indicating that the post was liked successfully.
     */
    public String likePost(String username, String postId) {
        // Retrieve the user from the database using the username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Retrieve the post from the database using the post ID
        Post post = postRepository.findByPostKey(postId)
                .orElseThrow(() -> new PostNotFoundException("Post Not Found!"));

        // Add the user to the set of users who liked the post
        post.getLikedByUsers().add(user);

        // Save the post to the database
        postRepository.save(post);

        // Send a notification for the liked post
        notificationService.sendLikedNotification(user, post);

        return "Post liked Successfully";
    }
}
