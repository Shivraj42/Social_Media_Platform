package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.Services.Impls.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @PostMapping("/send-request")
    public ResponseEntity sendRequest(@RequestParam("to") String receiverUsername){

        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username from the authentication object
        String senderUsername = authentication.getName();

        try{
            String response= friendshipService.sendRequest(senderUsername, receiverUsername);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/accept-request")
    public ResponseEntity acceptRequest(@RequestParam("to") String receiverUsername){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderUsername = authentication.getName();

        try{
            String response= friendshipService.acceptRequest(senderUsername, receiverUsername);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }
}
