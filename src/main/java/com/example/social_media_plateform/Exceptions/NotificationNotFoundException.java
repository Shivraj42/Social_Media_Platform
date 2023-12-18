package com.example.social_media_plateform.Exceptions;

public class NotificationNotFoundException extends RuntimeException{
    public NotificationNotFoundException(String message){
        super(message);
    }
}
