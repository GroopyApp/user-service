package app.groopy.userservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationSignInResponse {
    AuthenticationUserResponse authenticationUserResponse;
    String token;
}
