package app.groopy.userservice.infrastructure.repository.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FirebaseUpdateProfileRequest {

    private String idToken;
    private String displayName;
    private String photoUrl;
    private List<String> deleteAttribute;
    private Boolean returnSecureToken;
}
