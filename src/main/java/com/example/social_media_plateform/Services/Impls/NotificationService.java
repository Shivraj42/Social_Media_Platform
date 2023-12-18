package com.example.social_media_plateform.Services.Impls;

import com.example.social_media_plateform.Enums.NotificationType;
import com.example.social_media_plateform.Exceptions.NotificationNotFoundException;
import com.example.social_media_plateform.Models.Notification;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }



    public void sendFriendRequestNotification(User receiver, User sender, String message) {
        Notification notification= Notification.builder()
                .notificationType(NotificationType.FRIEND_REQUEST)
                .content(message)
                .user(receiver)
                .fromUser(sender)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    public void sendFriendRequestAcceptedNotification(User sender, User receiver, String message) {
        Notification notification= Notification.builder()
                .notificationType(NotificationType.FRIEND_REQUEST_ACCEPTED)
                .content(message)
                .user(sender)
                .fromUser(receiver)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    public String readNotification(Long notificationId){
        Notification notification= notificationRepository.findById(notificationId)
                .orElseThrow(()-> new NotificationNotFoundException("Notification Not Found!"));
        notification.setRead(true);
        return "Notification read successfully!";
    }
}
