package app.groopy.userservice.infrastructure.providers.models;

import lombok.Value;

@Value
public class FirebaseSignUpResponse {
    String idToken;
    String email;
    String refreshToken;
    String expiresIn;
    String localId;
}
