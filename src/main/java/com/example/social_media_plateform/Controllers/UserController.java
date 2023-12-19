package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.DTOs.responseDTOs.UserProfileResponseDTO;
import com.example.social_media_plateform.Services.Impls.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {
    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update-bio")
    public ResponseEntity updateBio(@RequestParam("bio") String bio){

        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username from the authentication object
        String username = authentication.getName();

        try{
            UserProfileResponseDTO response= userService.updateBio(username, bio);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-profile-pic")
    public ResponseEntity updateBio( @RequestPart("pic") MultipartFile file){

        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username from the authentication object
        String username = authentication.getName();

        try{
            UserProfileResponseDTO response= userService.updateProfilePic(username, file);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }



}
