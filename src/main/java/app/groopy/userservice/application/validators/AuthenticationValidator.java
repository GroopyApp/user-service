package app.groopy.userservice.application.validators;

import app.groopy.userservice.domain.exceptions.AuthenticationValidationException;
import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.infrastructure.services.ElasticsearchUserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationValidator {

    @Autowired
    private ElasticsearchUserService elasticSearchUserService;

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();


    public void validate(SignUpInternalRequest request) throws AuthenticationValidationException {
        if (!EMAIL_VALIDATOR.isValid(request.getEmail())) {
            throw new AuthenticationValidationException("email", request.getEmail());
        }
        if (request.getUsername().isEmpty()) {
            throw new AuthenticationValidationException("username cannot be null");
        }
        //TODO add more validations
    }

    public void validate(SignInInternalRequest request) throws AuthenticationValidationException {
        //TODO
    }
}
