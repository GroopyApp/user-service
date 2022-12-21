package app.groopy.userservice.infrastructure.repository.models;

import lombok.Value;

import java.util.List;

@Value
public class FirebaseUserProfileResponse {
    List<User> users;

    @Value
    public static class User {
        String localId;
        String email;
        Boolean emailVerified;
        String displayName;
        String photoUrl;
        String passwordHash;
        String validSince;
        Boolean disabled;
    }
}
