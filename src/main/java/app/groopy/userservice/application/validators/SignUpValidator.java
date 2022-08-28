package app.groopy.userservice.application.validators;

import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.infrastructure.elasticsearch.repository.ElasticsearchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignUpValidator {

    @Autowired
    private ElasticsearchUserRepository elasticSearchUserRepository;
    private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";

    public void validate(SignInInternalRequest request) {
        //TODO remember to add Pattern.CASE_INSENSITIVE on pattern compile
    }
}
