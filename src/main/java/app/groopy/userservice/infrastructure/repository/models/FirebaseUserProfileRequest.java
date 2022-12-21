package app.groopy.userservice.infrastructure.repository.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FirebaseUserProfileRequest {
    private String idToken;
}
