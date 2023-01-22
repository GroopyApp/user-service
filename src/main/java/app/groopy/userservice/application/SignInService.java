package app.groopy.userservice.application;

import app.groopy.providers.firebase.models.AuthenticationSignInRequest;
import app.groopy.userservice.application.mapper.ApplicationMapper;
import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.exceptions.SignInException;
import app.groopy.userservice.domain.models.SignInRequestDto;
import app.groopy.userservice.domain.models.SignInResponseDto;
import app.groopy.userservice.infrastructure.AuthenticationInfrastructureService;
import app.groopy.userservice.infrastructure.ElasticsearchInfrastructureService;
import app.groopy.userservice.infrastructure.exceptions.AuthenticationServiceException;
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
            ApplicationMapper mapper,
            AuthenticationInfrastructureService authenticationInfrastructureService,
            ElasticsearchInfrastructureService elasticsearchInfrastructureService) {
        super(validator, mapper, authenticationInfrastructureService, elasticsearchInfrastructureService);
    }

    @SneakyThrows({AuthenticationValidationException.class, SignInException.class})
    public SignInResponseDto perform(SignInRequestDto request) {
        validator.validate(request);
        try {
            //TODO send user status update to ES
            return mapper.map(authenticationInfrastructureService.signIn(AuthenticationSignInRequest.builder()
                            .email(request.getEmail())
                            .password(request.getPassword())
                    .build()));
        } catch (AuthenticationServiceException ex) {
            LOGGER.error(String.format("an error occurred trying to login user with email %s", request.getEmail()), ex);
            throw new SignInException(request, ex.getLocalizedMessage());
        }
    }
}
