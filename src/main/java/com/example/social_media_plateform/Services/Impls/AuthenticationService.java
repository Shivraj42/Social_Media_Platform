package com.example.social_media_plateform.Services.Impls;

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


    /**
     * Registers a new user with the provided sign-up details.
     *
     * @param request SignUpRequest containing user registration information.
     * @return A message instructing the user to check their email for a verification link.
     * @throws UserAlreadyExistsException Thrown if a user already exists with the provided email or username.
     */
    public String signup(SignUpRequest request) {
        // Check if the user already exists with the given email or username
        Optional<User> optionalUser = userRepository.findByEmailOrUsername(request.getEmail(), request.getUsername());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with that email or username!");
        }

        // Create a new user entity with the provided registration details
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        // Save the new user
        user = userService.save(user);

        // Create an email verification token for the new user
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.createVerificationToken(user);
        String token = emailVerificationToken.getToken();

        // Send a verification email to the user
        emailService.sendVerificationEmail(user.getEmail(), token);

        // Provide a response message for the user
        String response = "Check your email for the verification link and log in again.";

        return response;
    }



    /**
     * Verifies a user's email based on the provided verification token.
     *
     * @param token The email verification token.
     * @return A success message upon successful email verification.
     * @throws VerificationLinkNotValidException Thrown if the verification link is invalid or broken.
     */
    public String verifyEmail(String token) {
        // Initialize EmailVerificationToken
        EmailVerificationToken emailVerificationToken = null;

        // Check if the token is provided
        if (token != null) {
            // Retrieve EmailVerificationToken from the repository
            Optional<EmailVerificationToken> optionalEmailVerificationToken = emailVerificationTokenRepository.findByToken(token);

            // Throw an exception if the token is not found
            if (optionalEmailVerificationToken.isEmpty()) {
                throw new VerificationLinkNotValidException("The link is invalid or broken!");
            }

            // Assign the retrieved token to the variable
            emailVerificationToken = optionalEmailVerificationToken.get();
        } else {
            // Throw an exception if the token is null
            throw new VerificationLinkNotValidException("The link is invalid or broken!");
        }

        // Retrieve user from the token
        User user = emailVerificationToken.getUser();

        // Set the user as verified
        user.setVerified(true);

        // Save the updated user
        userRepository.save(user);

        // Return success message
        return "Email verified successfully! Now please sign in.";
    }



    /**
     * Handles user authentication for sign-in requests.
     *
     * @param request SignInRequest containing username or email and password.
     * @return JwtAuthenticationResponse containing a JWT for authenticated users.
     * @throws Exception Thrown if authentication fails or if the user has not verified their email.
     */
    public JwtAuthenticationResponse signin(SignInRequest request) throws Exception{
        // Authenticate username and password
        // It may throw exceptions
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsernameOrEmail(), request.getPassword()));

        // find the user by username or email given by user
        var user = userRepository.findByEmailOrUsername(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or username."));

        // check emai verification status
        if(!user.isVerified()){
            throw new EmailNotVerifiedException("User has not verified email!");
        }
        // send jwt for future reference
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

}
