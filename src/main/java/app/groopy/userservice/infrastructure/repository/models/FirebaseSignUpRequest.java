package app.groopy.userservice.infrastructure.repository.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseSignUpRequest {

    private String email;
    private String password;
    private Boolean returnSecureToken;
}
