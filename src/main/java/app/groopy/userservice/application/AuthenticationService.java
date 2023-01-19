package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.exceptions.SignInException;
import app.groopy.userservice.domain.exceptions.SignUpException;
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.domain.models.SignInInternalResponse;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider;
import app.groopy.userservice.infrastructure.services.AuthServiceProvider;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationValidator validator;
    private final AuthServiceProvider authServiceProvider;
    private final ElasticsearchProvider elasticsearchProvider;

    @Autowired
    public AuthenticationService(
            AuthenticationValidator validator,
            AuthServiceProvider authServiceProvider,
            ElasticsearchProvider elasticsearchProvider) {
        this.validator = validator;
        this.authServiceProvider = authServiceProvider;
        this.elasticsearchProvider = elasticsearchProvider;
    }

    @SneakyThrows({AuthenticationValidationException.class, SignUpException.class})
    public SignUpInternalResponse register(SignUpInternalRequest request) {
        validator.validate(request);
        try {
            SignUpInternalResponse response = authServiceProvider.signUp(request);
            try {
                elasticsearchProvider.save(response.getUser());
            } catch (Throwable ex) {
                LOGGER.error(
                        String.format("An error occurred trying to save user in ESDB, user registration will be rolled back: request:{%s}, error:{%s",
                                request,
                                ex.getLocalizedMessage()));
                authServiceProvider.deleteUser(response.getLocalId());
                throw new SignUpException(request, ex.getLocalizedMessage());
            }
            return response;
        } catch (Exception ex) {
            throw new SignUpException(request, ex.getLocalizedMessage());
        }
    }

    @SneakyThrows({AuthenticationValidationException.class, SignInException.class})
    public SignInInternalResponse login(SignInInternalRequest request) {
        validator.validate(request);
        try {
            return authServiceProvider.signIn(request);
        } catch (Exception ex) {
            throw new SignInException(request, ex.getLocalizedMessage());
        }
    }

    //FIXME DEV USE ONLY, REMOVE IT
    public void deleteAllUsers() {
        authServiceProvider.deleteAllUsers();
    }
}
