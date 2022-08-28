package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.SignUpValidator;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private SignUpValidator validator;

    //TODO implement business logic
    public SignUpInternalResponse register(SignUpInternalRequest request) {
        return null;
    }
}
