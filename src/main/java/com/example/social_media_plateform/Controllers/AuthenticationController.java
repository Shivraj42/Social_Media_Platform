package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.DTOs.requestDTOs.SignInRequest;
import com.example.social_media_plateform.DTOs.requestDTOs.SignUpRequest;
import com.example.social_media_plateform.DTOs.responseDTOs.JwtAuthenticationResponse;
import com.example.social_media_plateform.Exceptions.EmailNotVerifiedException;
import com.example.social_media_plateform.Exceptions.UserAlreadyExistsException;
import com.example.social_media_plateform.Exceptions.VerificationLinkNotValidException;
import com.example.social_media_plateform.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignUpRequest request) {
        try {
            String response = authenticationService.signup(request);
            return new ResponseEntity(response, HttpStatus.ACCEPTED);
        }
        catch (UserAlreadyExistsException e){
            String message= e.getMessage();
            return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity verifyEmail( @RequestParam("token") String token){
        try {
            String response= authenticationService.verifyEmail(token);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        catch (VerificationLinkNotValidException e){
            String response= e.getMessage();
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            String response = "Any other exception";
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }





    }

    @PostMapping("/signin")
    public ResponseEntity signin (@RequestBody SignInRequest request) {
        try{
            JwtAuthenticationResponse response = authenticationService.signin(request);
            return new ResponseEntity(response, HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            String response= e.getMessage();
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
    }
}
