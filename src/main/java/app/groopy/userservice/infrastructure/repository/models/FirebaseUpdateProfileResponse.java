package app.groopy.userservice.infrastructure.repository.models;

import lombok.Value;

@Value
public class FirebaseUpdateProfileResponse {
    String localId;
    String email;
    String displayName;
    String photoUrl;
    String passwordHash;
//    List<Object> providerUserInfo;
    String idToken;
    String refreshToken;
    String expiresIn;
}
