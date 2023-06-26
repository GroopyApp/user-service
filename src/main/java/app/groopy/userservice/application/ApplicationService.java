package app.groopy.userservice.application;

import app.groopy.userservice.application.exceptions.ApplicationException;
import app.groopy.userservice.domain.models.*;
import app.groopy.userservice.domain.services.AuthenticationService;
import app.groopy.userservice.domain.resolver.DomainExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public ApplicationService(
            AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public SignInResponseDto signIn(SignInRequestDto request) throws ApplicationException {
        try {
            return authenticationService.signIn(request);
        } catch (Exception e) {
            LOGGER.error(String.format("an error occurred trying to login user with email %s", request.getEmail()), e);
            throw DomainExceptionResolver.resolve(e);
        }
    }

    public SignUpResponseDto signUp(SignUpRequestDto request) throws ApplicationException {
        try {
            return authenticationService.signUp(request);
        } catch (Exception e) {
            LOGGER.error(String.format("an error occurred trying to register user with email %s", request.getEmail()), e);
            throw DomainExceptionResolver.resolve(e);
        }
    }

    public SignInResponseDto oAuth(OAuthRequestDto request) throws ApplicationException {
        try {
            return authenticationService.signInWithToken(request);
        } catch (Exception e) {
            LOGGER.error(String.format("an error occurred trying to login user with OAuth token: %s and provider: %s",
                    request.getToken(), request.getProvider()), e);
            throw DomainExceptionResolver.resolve(e);
        }
    }
}
