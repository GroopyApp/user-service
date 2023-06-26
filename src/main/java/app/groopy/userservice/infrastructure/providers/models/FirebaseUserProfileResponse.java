package app.groopy.userservice.infrastructure.providers.models;

import lombok.Value;

import java.util.List;

@Value
public class FirebaseUserProfileResponse {

    List<ProviderUser> users;

    @Value
    public static class ProviderUser {
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
