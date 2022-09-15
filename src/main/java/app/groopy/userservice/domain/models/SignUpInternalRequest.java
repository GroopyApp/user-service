package app.groopy.userservice.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpInternalRequest {
    String name;
    String email;
    String password;
    String photoUrl;
}
