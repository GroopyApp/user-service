package app.groopy.userservice.application.validators;

import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.models.SignInRequestDto;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.infrastructure.ElasticsearchInfrastructureService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationValidator {

    @Autowired
    private ElasticsearchInfrastructureService elasticsearchInfrastructureService;

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();


    public void validate(SignUpRequestDto request) throws AuthenticationValidationException {
        if (!EMAIL_VALIDATOR.isValid(request.getEmail())) {
            throw new AuthenticationValidationException("email", request.getEmail());
        }
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new AuthenticationValidationException("username cannot be null");
        }
        //TODO add more validations
    }

    public void validate(SignInRequestDto request) throws AuthenticationValidationException {
        //TODO
    }
}
