package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.exceptions.SignInException;
import app.groopy.userservice.domain.exceptions.SignUpException;
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.domain.models.SignInInternalResponse;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;
import app.groopy.userservice.infrastructure.services.AuthServiceProvider;
import app.groopy.userservice.infrastructure.services.ElasticsearchUserService;
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
    private final ElasticsearchUserService elasticsearchUserService;

    @Autowired
    public AuthenticationService(
            AuthenticationValidator validator,
            AuthServiceProvider authServiceProvider,
            ElasticsearchUserService elasticsearchUserService) {
        this.validator = validator;
        this.authServiceProvider = authServiceProvider;
        this.elasticsearchUserService = elasticsearchUserService;
    }

    @SneakyThrows({AuthenticationValidationException.class, SignUpException.class})
    public SignUpInternalResponse register(SignUpInternalRequest request) {
        validator.validate(request);
        try {
            SignUpInternalResponse response = authServiceProvider.signUp(request);
            try {
                elasticsearchUserService.save(response.getUser());
            } catch (Throwable ex) {
                LOGGER.error(
                        String.format("An error occurred trying to save user in ESDB, user registration will be rolled back: request:{%s}, error:{%s",
                                request,
                                ex.getLocalizedMessage()));
                authServiceProvider.deleteUser(response.getUser().getEmail());
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
