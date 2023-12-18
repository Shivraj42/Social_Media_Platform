package com.example.social_media_plateform.Models;

import com.example.social_media_plateform.Enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "notification_date", nullable = false)
    private LocalDateTime notificationDate;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @ManyToOne
    @JoinColumn(name = "from_user")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "from_post")
    private Post fromPost;

    // Reference to Comment for comment notifications
    @ManyToOne
    @JoinColumn(name = "from_comment")
    private Comment fromComment;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

}
