package app.groopy.userservice.infrastructure.repository.models;

import app.groopy.userservice.domain.models.common.UserDetailsDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseUserDetailsResponse {
    UserDetailsDto response;
    String token;
}
