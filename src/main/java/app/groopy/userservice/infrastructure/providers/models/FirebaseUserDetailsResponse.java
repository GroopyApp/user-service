package app.groopy.userservice.infrastructure.providers.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseUserDetailsResponse {
    FirebaseUserResponse response;
    String token;
}
