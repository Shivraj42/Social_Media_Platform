package com.example.social_media_plateform.Services.Impls;

import com.example.social_media_plateform.Enums.FriendshipStatus;
import com.example.social_media_plateform.Exceptions.FriendshipNotFoundException;
import com.example.social_media_plateform.Exceptions.UserNotFoundException;
import com.example.social_media_plateform.Models.Friendship;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.FriendshipRepository;
import com.example.social_media_plateform.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    private final NotificationService notificationService;

    private final UserRepository userRepository;


    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository,
                             NotificationService notificationService,
                             UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }


    public String sendRequest(String senderUsername, String receiverUsername) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));

        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));

        Optional<Friendship> optionalFriendship= friendshipRepository.findByUser1AndUser2(sender, receiver);

        if (optionalFriendship.isEmpty() || optionalFriendship.get().getStatus() != FriendshipStatus.ACCEPTED) {
            Friendship friendship = Friendship.builder()
                    .user1(sender)
                    .user2(receiver)
                    .status(FriendshipStatus.PENDING)
                    .build();

            friendshipRepository.save(friendship);

            String message= senderUsername+" sent you the friend request!";
            // Send notification to the receiver
            notificationService.sendFriendRequestNotification(receiver, sender, message);

            return "Request sent Successfully!";
        }
        else return "You are already friend!";

    }

    public String acceptRequest(String senderUsername, String receiverUsername) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));

        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));

        Friendship friendship = friendshipRepository.findByUser1AndUser2(sender, receiver)
                .orElseThrow(() -> new FriendshipNotFoundException("Friendship not found"));

        if (friendship.getStatus() == FriendshipStatus.PENDING) {
            friendship.setStatus(FriendshipStatus.ACCEPTED);
            friendshipRepository.save(friendship);

            String message= receiverUsername+" accepted your friend request!";
            // Send notification to the sender
            notificationService.sendFriendRequestAcceptedNotification(sender, receiver, message);
            return "Request accepted Successfully!";
        }

        return "Request already accepted!";

    }
}
