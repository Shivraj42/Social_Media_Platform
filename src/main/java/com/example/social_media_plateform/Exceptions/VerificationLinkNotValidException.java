package com.example.social_media_plateform.Exceptions;

public class VerificationLinkNotValidException extends RuntimeException{

    public VerificationLinkNotValidException(String message){
        super(message);
    }
}
