package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.exceptions.SignInException;
import app.groopy.userservice.domain.models.SignInRequestDto;
import app.groopy.userservice.domain.models.SignInResponseDto;
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider;
import app.groopy.userservice.infrastructure.services.AuthServiceProvider;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignInService extends AuthenticationService<SignInRequestDto, SignInResponseDto> {

    private final Logger LOGGER = LoggerFactory.getLogger(SignInService.class);

    @Autowired
    public SignInService(
            AuthenticationValidator validator,
            AuthServiceProvider authServiceProvider,
            ElasticsearchProvider elasticsearchProvider) {
        super(validator, authServiceProvider, elasticsearchProvider);
    }

    @SneakyThrows({AuthenticationValidationException.class, SignInException.class})
    public SignInResponseDto perform(SignInRequestDto request) {
        validator.validate(request);
        try {
            //TODO send user status update to ES
            return authServiceProvider.signIn(request);
        } catch (Exception ex) {
            throw new SignInException(request, ex.getLocalizedMessage());
        }
    }
}
