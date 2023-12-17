package com.example.social_media_plateform.Repositories;

import com.example.social_media_plateform.Models.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

   public Optional<EmailVerificationToken> findByToken(String token);
}
