package app.groopy.userservice.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignInRequestDto {
    String email;
    String password;
}
