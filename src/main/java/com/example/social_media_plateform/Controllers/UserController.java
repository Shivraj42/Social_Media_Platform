package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.DTOs.responseDTOs.UserProfileResponseDTO;
import com.example.social_media_plateform.Services.Impls.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update-bio")
    public ResponseEntity updateBio(@RequestParam("user") String username,
                                    @RequestParam("bio") String bio){

        try{
            UserProfileResponseDTO response= userService.updateBio(username, bio);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }



}
