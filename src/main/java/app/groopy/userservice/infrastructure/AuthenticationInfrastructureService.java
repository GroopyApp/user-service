package app.groopy.userservice.infrastructure;

import app.groopy.commons.infrastructure.providers.AuthenticationProvider;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignInRequest;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignInResponse;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignUpRequest;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignUpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationInfrastructureService {

    private final AuthenticationProvider provider;

    @Autowired
    public AuthenticationInfrastructureService(AuthenticationProvider provider) {
        this.provider = provider;
    }

    public void deleteAllUsers() {
        try {
            provider.deleteAllUsers();
        } catch (Exception e) {
            //TODO add infrastructure exceptions
            throw new RuntimeException(e);
        }
    }

    public AuthenticationSignInResponse signIn(AuthenticationSignInRequest request) {
        try {
            return provider.signIn(request);
        } catch (Exception e) {
            //TODO add infrastructure exceptions
            throw new RuntimeException(e);
        }
    }

    public AuthenticationSignUpResponse signUp(AuthenticationSignUpRequest request) {
        try {
            return provider.signUp(request);
        } catch (Exception e) {
            //TODO add infrastructure exceptions
            throw new RuntimeException(e);
        }    }

    public void deleteUser(String localId) {
        try {
            provider.deleteUser(localId);
        } catch (Exception e) {
            //TODO add infrastructure exceptions
            throw new RuntimeException(e);
        }
    }
}
