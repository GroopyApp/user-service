package app.groopy.userservice.infrastructure.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    String email;
    String name;
    String photoUrl;
}
