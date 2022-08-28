package app.groopy.userservice.domain.models;

import app.groopy.userservice.domain.models.common.UserDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsInternalResponse {
    private UserDetails user;
}
