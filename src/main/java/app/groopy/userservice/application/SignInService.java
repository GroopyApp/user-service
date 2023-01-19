package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.exceptions.SignInException;
import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.domain.models.SignInInternalResponse;
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider;
import app.groopy.userservice.infrastructure.services.AuthServiceProvider;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignInService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignInService.class);

    private final AuthenticationValidator validator;

    private final AuthServiceProvider authServiceProvider;
    private final ElasticsearchProvider elasticsearchProvider;

    @Autowired
    public SignInService(
            AuthenticationValidator validator,
            AuthServiceProvider authServiceProvider,
            ElasticsearchProvider elasticsearchProvider) {
        this.validator = validator;
        this.authServiceProvider = authServiceProvider;
        this.elasticsearchProvider = elasticsearchProvider;
    }

    @SneakyThrows({AuthenticationValidationException.class, SignInException.class})
    public SignInInternalResponse login(SignInInternalRequest request) {
        validator.validate(request);
        try {
            //TODO send user status update to ES
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
