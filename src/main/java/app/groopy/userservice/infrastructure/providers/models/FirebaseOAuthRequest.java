package app.groopy.userservice.infrastructure.providers.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FirebaseOAuthRequest {
    private String requestUri;
    private String postBody;
    private Boolean returnSecureToken;
    private Boolean returnIdpCredential;
}
