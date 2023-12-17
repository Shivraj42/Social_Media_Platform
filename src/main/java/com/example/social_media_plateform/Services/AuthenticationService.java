package com.example.social_media_plateform.Services;

import com.example.social_media_plateform.DTOs.requestDTOs.SignInRequest;
import com.example.social_media_plateform.DTOs.requestDTOs.SignUpRequest;
import com.example.social_media_plateform.DTOs.responseDTOs.JwtAuthenticationResponse;
import com.example.social_media_plateform.Enums.Role;
import com.example.social_media_plateform.Exceptions.EmailNotVerifiedException;
import com.example.social_media_plateform.Exceptions.UserAlreadyExistsException;
import com.example.social_media_plateform.Exceptions.VerificationLinkNotValidException;
import com.example.social_media_plateform.Models.EmailVerificationToken;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.EmailVerificationTokenRepository;
import com.example.social_media_plateform.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;


    public String signup(SignUpRequest request) {
        Optional<User> optionalUser= userRepository.findByEmailOrUsername(request.getEmail(), request.getUsername());
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistsException("User already exist with that email or username!");
        }

        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        user = userService.save(user);

        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.createVerificationToken(user);
        String token= emailVerificationToken.getToken();
        emailService.sendVerificationEmail(user.getEmail(),token);

        String response= "Check the emails for verification link and login again";

        return response;
    }

    public String verifyEmail(String token){

        EmailVerificationToken emailVerificationToken= null;

        if(token!=null){
            Optional<EmailVerificationToken> optionalEmailVerificationToken= emailVerificationTokenRepository.findByToken(token);
            if(optionalEmailVerificationToken.isEmpty()){
                throw new VerificationLinkNotValidException("The link is invalid or broken!");
            }
            emailVerificationToken= optionalEmailVerificationToken.get();
        }
        else{
            throw new VerificationLinkNotValidException("The link is invalid or broken!");
        }

        User user= emailVerificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        return "Email verified Successfully! Now please sign in";
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));


        var user = userRepository.findByEmailOrUsername(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        if(!user.isVerified()){
            throw new EmailNotVerifiedException("User has not verified email!");
        }

        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

}
