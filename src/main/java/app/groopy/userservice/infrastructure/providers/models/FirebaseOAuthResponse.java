package app.groopy.userservice.infrastructure.providers.models;

import lombok.Value;

@Value
public class FirebaseOAuthResponse {
    String federatedId;
    String providerId;
    String localId;
    Boolean emailVerified;
    String email;
    String oauthIdToken;
    String oauthAccessToken;
    String oauthTokenSecret;
    String rawUserInfo;
    String firstName;
    String lastName;
    String fullName;
    String displayName;
    String photoUrl;
    String idToken;
    String refreshToken;
    String expiresIn;
    Boolean needConfirmation;
}
