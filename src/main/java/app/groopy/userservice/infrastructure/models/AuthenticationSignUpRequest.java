package app.groopy.userservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationSignUpRequest {
    private String email;
    private String password;
    private Boolean returnSecureToken;
    private String username;
    private String photoUrl;
}
