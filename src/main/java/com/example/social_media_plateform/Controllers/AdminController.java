package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.DTOs.responseDTOs.PostResponseDTO;
import com.example.social_media_plateform.DTOs.responseDTOs.UserProfileResponseDTO;
import com.example.social_media_plateform.Services.Impls.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/disable-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity disableUser(@RequestParam("user") String username){
        try{
            String response= userService.disableUser(username);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/enable-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity enableUser(@RequestParam("user") String username){
        try{
            String response= userService.enableUser(username);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/view-profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity viewProfile(@RequestParam("user") String username){
        try{
             UserProfileResponseDTO response= userService.viewProfile(username);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/view-all-post")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity viewAllPost(@RequestParam("user") String username){
        try{
            List<PostResponseDTO> response= userService.viewAllPost(username);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

}
