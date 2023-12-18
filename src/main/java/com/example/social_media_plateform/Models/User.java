package com.example.social_media_plateform.Models;

import com.example.social_media_plateform.Enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String firstName;

    String lastName;

    @Email
    @Column(unique = true, nullable = false)
    String email;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Enumerated(EnumType.STRING)
    Role role;

    String bio;

    @Column(name = "profile_pic_url")
    String profilePicUrl;

    boolean verified;

    boolean Disabled;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
     Set<Follow> followers= new HashSet<>();

    @OneToMany(mappedBy = "following")
     Set<Follow> following= new HashSet<>();

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
     Set<Friendship> friendships= new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     List<Post> posts= new ArrayList<>();

    @ManyToMany(mappedBy = "likedByUsers", cascade = CascadeType.ALL)
     Set<Post> likedPosts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     List<Notification> notifications= new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
     List<Comment> comments= new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
     List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
     List<Message> receivedMessages;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
