package app.groopy.userservice.infrastructure.repository.models;

import lombok.Value;

@Value
public class FirebaseSignUpResponse {

    String idToken;
    String email;
    String refreshToken;
    String expiresIn;
    String localId;
}
