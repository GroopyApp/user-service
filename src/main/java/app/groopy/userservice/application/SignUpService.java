package app.groopy.userservice.application;

import app.groopy.providers.firebase.models.AuthenticationSignUpRequest;
import app.groopy.userservice.application.mapper.ApplicationMapper;
import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.exceptions.SignUpException;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.domain.models.SignUpResponseDto;
import app.groopy.userservice.infrastructure.AuthenticationInfrastructureService;
import app.groopy.userservice.infrastructure.ElasticsearchInfrastructureService;
import app.groopy.userservice.infrastructure.exceptions.AuthenticationServiceException;
import app.groopy.userservice.infrastructure.exceptions.ElasticsearchServiceException;
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
                elasticsearchInfrastructureService.save(mapper.map(response.getUser()));
            } catch (ElasticsearchServiceException ex) {
                LOGGER.error(
                        String.format("An error occurred trying to save user in ESDB, user registration will be rolled back: request:{%s}, error:{%s",
                                request,
                                ex.getLocalizedMessage()));
                try {
                    authenticationInfrastructureService.deleteUser(response.getLocalId());
                } catch (AuthenticationServiceException e) {
                    LOGGER.error(String.format("An error occurred trying to delete the user that was saved in firebase but not in elasticsearch. A manual operation will be required for it: user localId: %s", response.getLocalId()));
                }
                throw new SignUpException(request, ex.getLocalizedMessage());
            }
            return response;
        } catch (AuthenticationServiceException e) {
            throw new SignUpException(request, e.getLocalizedMessage());
        }
    }
}
