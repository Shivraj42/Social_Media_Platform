package com.example.social_media_plateform.Exceptions;

public class FriendshipNotFoundException extends RuntimeException{
    public FriendshipNotFoundException(String message){
        super(message);
    }
}
