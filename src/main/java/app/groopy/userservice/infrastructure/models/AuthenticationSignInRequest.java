package app.groopy.userservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationSignInRequest {
    private String email;
    private String password;
}
