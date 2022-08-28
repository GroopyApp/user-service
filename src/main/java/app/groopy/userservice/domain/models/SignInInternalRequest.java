package app.groopy.userservice.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInInternalRequest {
    String credential;
    String password;
}
