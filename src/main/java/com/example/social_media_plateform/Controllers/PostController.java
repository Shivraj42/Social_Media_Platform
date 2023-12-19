package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.DTOs.requestDTOs.PostRequestDTO;
import com.example.social_media_plateform.DTOs.responseDTOs.PostResponseDTO;
import com.example.social_media_plateform.DTOs.responseDTOs.UserProfileResponseDTO;
import com.example.social_media_plateform.Enums.PrivacySetting;
import com.example.social_media_plateform.Services.Impls.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user/post")
public class PostController {


    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity createPost(@RequestPart("request")PostRequestDTO postRequestDTO,
                                     @RequestPart("file")MultipartFile file){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try{
            PostResponseDTO response= postService.createPost( username, postRequestDTO, file);
            return  new ResponseEntity(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/repost")
    public ResponseEntity repostPost(@RequestParam("post") String postId,
                                     @RequestParam("privacy")PrivacySetting privacySetting) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try{
            PostResponseDTO response= postService.repostPost(username, postId, privacySetting);
            return  new ResponseEntity(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/view-all-posts")
    public ResponseEntity viewAllPosts(@RequestParam("user") String targetUsername ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String myUsername = authentication.getName();

        try{
            List<PostResponseDTO> response= postService.viewAllPosts( myUsername,targetUsername);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/feed")
    public ResponseEntity generateFeed(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try{
            List<PostResponseDTO> response= postService.generateFeed(username);
            return  new ResponseEntity(response, HttpStatus.OK);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }
}
