package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.DTOs.requestDTOs.PostRequestDTO;
import com.example.social_media_plateform.Enums.PrivacySetting;
import com.example.social_media_plateform.Services.Impls.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        try{
            String response= postService.createPost( postRequestDTO, file);
            return  new ResponseEntity(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/repost")
    public ResponseEntity repostPost(@RequestParam("user") String username,
                                     @RequestParam("post") String postId,
                                     @RequestParam("privacy")PrivacySetting privacySetting) {
        try{
            String response= postService.repostPost(username, postId, privacySetting);
            return  new ResponseEntity(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            String response= e.getMessage();
            return  new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }
}
