package app.groopy.userservice.domain.models.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsDto {
    String userId;
    String name;
    String surname;
    String email;
    String photoUrl;
}
