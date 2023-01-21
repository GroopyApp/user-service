package app.groopy.userservice.application;

import app.groopy.commons.infrastructure.repository.models.firebase.AuthenticationSignUpRequest;
import app.groopy.userservice.application.mapper.ApplicationMapper;
import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.exceptions.SignUpException;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.domain.models.SignUpResponseDto;
import app.groopy.userservice.infrastructure.AuthenticationInfrastructureService;
import app.groopy.userservice.infrastructure.ElasticsearchInfrastructureService;
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
            ApplicationMapper mapper,
            AuthenticationInfrastructureService authenticationInfrastructureService,
            ElasticsearchInfrastructureService elasticsearchInfrastructureService) {
        super(validator, mapper, authenticationInfrastructureService, elasticsearchInfrastructureService);
    }

    @SneakyThrows({AuthenticationValidationException.class, SignUpException.class})
    public SignUpResponseDto perform(SignUpRequestDto request) {
        validator.validate(request);
        try {
            SignUpResponseDto response = mapper.map(authenticationInfrastructureService.signUp(AuthenticationSignUpRequest.builder()
                            .email(request.getEmail())
                            .password(request.getPassword())
                            .username(request.getUsername())
                            .photoUrl(request.getPhotoUrl())
                    .build()));
            try {
                elasticsearchInfrastructureService.save(response.getUser());
            } catch (Throwable ex) {
                LOGGER.error(
                        String.format("An error occurred trying to save user in ESDB, user registration will be rolled back: request:{%s}, error:{%s",
                                request,
                                ex.getLocalizedMessage()));
                authenticationInfrastructureService.deleteUser(response.getLocalId());
                throw new SignUpException(request, ex.getLocalizedMessage());
            }
            return response;
        } catch (Exception ex) {
            throw new SignUpException(request, ex.getLocalizedMessage());
        }
    }
}
