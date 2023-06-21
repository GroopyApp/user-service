package app.groopy.userservice.infrastructure.providers.models;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseUpdateProfileRequest {
    private String idToken;
    private String displayName;
    private String photoUrl;
    private List<String> deleteAttribute;
    private Boolean returnSecureToken;

}
