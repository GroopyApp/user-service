package app.groopy.userservice.infrastructure.repository.models.firebase;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseSignInRequest {

    private String email;
    private String password;
    private Boolean returnSecureToken;
}
