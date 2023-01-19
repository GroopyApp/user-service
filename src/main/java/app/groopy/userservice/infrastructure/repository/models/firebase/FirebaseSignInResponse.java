package app.groopy.userservice.infrastructure.repository.models.firebase;

import lombok.Value;

@Value
public class FirebaseSignInResponse {

    String idToken;
    String email;
    String refreshToken;
    String expiresIn;
    String localId;
}
