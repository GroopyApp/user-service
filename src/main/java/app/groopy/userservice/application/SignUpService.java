package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.exceptions.SignUpException;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.domain.models.SignUpResponseDto;
import app.groopy.userservice.infrastructure.providers.AuthenticationProvider;
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService extends AuthenticationService<SignUpRequestDto, SignUpResponseDto> {

    private final Logger LOGGER = LoggerFactory.getLogger(SignUpService.class);

    @Autowired
    public SignUpService(
            AuthenticationValidator validator,
            AuthenticationProvider authenticationProvider,
            ElasticsearchProvider elasticsearchProvider) {
        super(validator, authenticationProvider, elasticsearchProvider);
    }

    @SneakyThrows({AuthenticationValidationException.class, SignUpException.class})
    public SignUpResponseDto perform(SignUpRequestDto request) {
        validator.validate(request);
        try {
            SignUpResponseDto response = authenticationProvider.signUp(request);
            try {
                elasticsearchProvider.save(response.getUser());
            } catch (Throwable ex) {
                LOGGER.error(
                        String.format("An error occurred trying to save user in ESDB, user registration will be rolled back: request:{%s}, error:{%s",
                                request,
                                ex.getLocalizedMessage()));
                authenticationProvider.deleteUser(response.getLocalId());
                throw new SignUpException(request, ex.getLocalizedMessage());
            }
            return response;
        } catch (Exception ex) {
            throw new SignUpException(request, ex.getLocalizedMessage());
        }
    }
}
