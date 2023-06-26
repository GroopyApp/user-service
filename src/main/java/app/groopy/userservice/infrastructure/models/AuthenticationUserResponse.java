package app.groopy.userservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationUserResponse {
    String email;
    String name;
    String photoUrl;
}
