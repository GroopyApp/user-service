package app.groopy.userservice.infrastructure;

import app.groopy.commons.infrastructure.providers.AuthenticationProvider;
import app.groopy.commons.infrastructure.providers.exceptions.FirebaseOperationError;
import app.groopy.commons.infrastructure.providers.exceptions.FirebaseProviderException;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignInRequest;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignInResponse;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignUpRequest;
import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignUpResponse;
import app.groopy.userservice.infrastructure.exceptions.AuthenticationServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationInfrastructureService {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationInfrastructureService.class);

    private final AuthenticationProvider provider;

    @Autowired
    public AuthenticationInfrastructureService(AuthenticationProvider provider) {
        this.provider = provider;
    }

    public void deleteAllUsers() throws AuthenticationServiceException {
        try {
            provider.deleteAllUsers();
        } catch (FirebaseProviderException e) {
            LOGGER.error("An error occurred trying to call firebase.deleteAllUsers", e);
            throw new AuthenticationServiceException("An error occurred trying to delete all users", e.getError());
        }
    }

    public AuthenticationSignInResponse signIn(AuthenticationSignInRequest request) throws AuthenticationServiceException {
        try {
            return provider.signIn(request);
        } catch (FirebaseProviderException e) {
            LOGGER.error("An error occurred trying to call firebase.signIn", e);
            throw new AuthenticationServiceException(
                    String.format("An error occurred trying to login user with email %s", request.getEmail()),
                    e.getError());
        } catch (IOException e) {
            LOGGER.error("An unexpected error occurred trying to call firebase.signIn", e);
            throw new AuthenticationServiceException("An error occurred calling firebase", FirebaseOperationError.UNKNOWN);
        }
    }

    public AuthenticationSignUpResponse signUp(AuthenticationSignUpRequest request) throws AuthenticationServiceException {
        try {
            return provider.signUp(request);
        } catch (FirebaseProviderException e) {
            LOGGER.error("An error occurred trying to call firebase.signUp", e);
            throw new AuthenticationServiceException(
                    String.format("An error occurred trying to login user with email %s", request.getEmail()),
                    e.getError());
        } catch (IOException e) {
            LOGGER.error("An unexpected error occurred trying to call firebase.signUp", e);
            throw new AuthenticationServiceException("An error occurred calling firebase", FirebaseOperationError.UNKNOWN);
        }
    }

    public void deleteUser(String localId) throws AuthenticationServiceException {
        try {
            provider.deleteUser(localId);
        } catch (FirebaseProviderException e) {
            LOGGER.error("An error occurred trying to call firebase.deleteUser", e);
            throw new AuthenticationServiceException(
                    String.format("An error occurred trying to delete user with localId %s", localId),
                    e.getError());
        }
    }
}
