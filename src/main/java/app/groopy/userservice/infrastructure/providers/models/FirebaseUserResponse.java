package app.groopy.userservice.infrastructure.providers.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseUserResponse {
    String userId;
    String email;
    String photoUrl;
    String localId;
    Boolean emailVerified;
    String displayName;
    String validSince;
    Boolean disabled;
}