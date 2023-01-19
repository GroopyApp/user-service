package app.groopy.userservice.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpInternalRequest {
    String username;
    String email;
    String password;
    String photoUrl;
}
