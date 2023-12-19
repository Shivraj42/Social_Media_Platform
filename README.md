# Social Media Platform

The Social Media Platform project aims to create a robust and user-friendly social networking application, providing individuals with a versatile and engaging platform for connecting, sharing, and communicating.

## Table of Contents

- [User Authentication and Authorization](#user-authentication-and-authorization)
  - [User Registration and Profiles](#user-registration-and-profiles)
  - [Authentication](#authentication)
  - [Authorization](#authorization)
- [Content Management](#content-management)
  - [Posting and Sharing](#posting-and-sharing)
  - [Privacy Settings](#privacy-settings)
  - [Media Storage](#media-storage)
- [Social Features](#social-features)
  - [Friendship and Following](#friendship-and-following)
  - [Activity Feed](#activity-feed)
  - [Messaging](#messaging)
- [Analytics](#analytics)

## User Authentication and Authorization

### User Registration and Profiles

- Users can create accounts with unique usernames and email addresses.
- User profiles are implemented with customizable information (bio, profile picture, etc.).
- Users can edit and update their profiles.

### Authentication

- Secure authentication system with email verification.
- Users can log in using username, password, and JWT (JSON Web Token) authentication.

### Authorization

- Roles defined (e.g., regular user, admin) with different levels of access.
- Admin users can disable, delete, and view all accounts and posts.

## Content Management

### Posting and Sharing

- Users can create posts with text, images, and videos.
- Options for users to comment on posts.
- Reposting functionalities are implemented (reposting adds the post to their posts).

### Privacy Settings

- Customizable privacy settings for user posts (public, private).
  - Public: Everyone can see the posts.
  - Private: Only friends can see the posts.

### Media Storage

- Files are saved in S3, and the URL of the files is stored where required.

## Social Features

### Friendship and Following

- Users can send and accept friend requests.
- "Follow" system implemented for users to receive updates from others without a reciprocal connection.

### Activity Feed

- Personalized activity feed showing posts from friends and followed users.
- Features like notifications for likes, comments, and friend requests are included.


## Technology Stack

### Backend

- Java (Spring Boot)

### Database

- Postgres

##Database ER Diagram
![smdb - public](https://github.com/Shivraj42/Social_Media_Platform/assets/133359705/8e4bdb3c-7b4f-416e-aa7e-b4c0ef028ee6)
