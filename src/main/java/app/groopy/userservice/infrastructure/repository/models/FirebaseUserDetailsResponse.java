package app.groopy.userservice.infrastructure.repository.models;

import app.groopy.userservice.domain.models.common.UserDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseUserDetailsResponse {
    UserDetails response;
    String token;
}
