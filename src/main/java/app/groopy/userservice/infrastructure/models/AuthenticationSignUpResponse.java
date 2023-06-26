package app.groopy.userservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationSignUpResponse {
    AuthenticationUserResponse authenticationUserResponse;
    String localId;
    String token;
}
