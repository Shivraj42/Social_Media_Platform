package com.example.social_media_plateform.Services;

import com.example.social_media_plateform.Models.EmailVerificationToken;
import com.example.social_media_plateform.Models.User;
import com.example.social_media_plateform.Repositories.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class  EmailVerificationTokenService {

    @Autowired
    private EmailVerificationTokenRepository tokenRepository;

    public EmailVerificationToken createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(1);   // 1 day from now

        EmailVerificationToken emailVerificationToken = EmailVerificationToken.builder()
                .user(user)
                .token(token)
                .expiresAt(expiresAt)
                .build();

        return tokenRepository.save(emailVerificationToken);
    }


}