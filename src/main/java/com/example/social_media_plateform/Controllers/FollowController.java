package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.Services.Impls.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class FollowController {
    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }



    @PostMapping("/follow")
    public ResponseEntity followUser(@RequestParam("follower") String follower,
                                     @RequestParam("following") String following){

        try{
            String response= followService.followUser(follower, following);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity unfollowUser(@RequestParam("follower") String follower,
                                       @RequestParam("followed") String followed){

        try{
            String response= followService.unfollowUser(follower, followed);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }
}
