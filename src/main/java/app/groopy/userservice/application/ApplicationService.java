package app.groopy.userservice.application;

import app.groopy.userservice.application.exceptions.ApplicationException;
import app.groopy.userservice.domain.models.SignInRequestDto;
import app.groopy.userservice.domain.models.SignInResponseDto;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.domain.models.SignUpResponseDto;
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
            var result = authenticationService.signIn(request);
            //TODO: add log here
            LOGGER.info("add log here {}", result);
            return result;
        } catch (Exception e) {
            LOGGER.error(String.format("an error occurred trying to login user with email %s", request.getEmail()), e);
            throw DomainExceptionResolver.resolve(e);
        }
    }

    public SignUpResponseDto signUp(SignUpRequestDto request) throws ApplicationException {
        try {
            var result = authenticationService.signUp(request);
            //TODO: add log here
            LOGGER.info("add log here {}", result);
            return result;
        } catch (Exception e) {
            LOGGER.error(String.format("an error occurred trying to register user with email %s", request.getEmail()), e);
            throw DomainExceptionResolver.resolve(e);
        }
    }
}
