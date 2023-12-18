package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.Services.Impls.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity sendRequest(@RequestParam("from") String senderUsername,
                                      @RequestParam("to") String receiverUsername){
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
    public ResponseEntity acceptRequest(@RequestParam("from") String senderUsername,
                                      @RequestParam("to") String receiverUsername){
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
