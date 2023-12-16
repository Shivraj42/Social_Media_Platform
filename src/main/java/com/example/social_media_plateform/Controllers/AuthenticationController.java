package com.example.social_media_plateform.Controllers;

import com.example.social_media_plateform.DTOs.requestDTOs.SignInRequest;
import com.example.social_media_plateform.DTOs.requestDTOs.SignUpRequest;
import com.example.social_media_plateform.DTOs.responseDTOs.JwtAuthenticationResponse;
import com.example.social_media_plateform.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return authenticationService.signup(request);
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return authenticationService.signin(request);
    }
}
