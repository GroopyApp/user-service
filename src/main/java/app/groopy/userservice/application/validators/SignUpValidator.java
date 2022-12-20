package app.groopy.userservice.application.validators;

import app.groopy.userservice.domain.exceptions.SignUpValidationException;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.infrastructure.services.ElasticsearchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SignUpValidator {

    @Autowired
    private ElasticsearchUserService elasticSearchUserService;
    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    public void validate(SignUpInternalRequest request) throws SignUpValidationException {
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new SignUpValidationException("email", request.getEmail());
        }

        //TODO add more validations
    }
}
