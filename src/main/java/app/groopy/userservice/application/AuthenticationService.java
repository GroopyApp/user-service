package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.domain.models.SignInInternalResponse;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;
import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.services.ElasticsearchUserService;
import app.groopy.userservice.infrastructure.services.FirebaseService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationValidator validator;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private ElasticsearchUserService elasticsearchUserService;

    @SneakyThrows
    public SignUpInternalResponse register(SignUpInternalRequest request) {
        validator.validate(request);

        SignUpInternalResponse response = firebaseService.signUp(request);

        elasticsearchUserService.save(UserDetails.builder()
                        .userId(response.getUser().getUserId())
                        .email(response.getUser().getEmail())
                .build());

        return response;
    }

    public void deleteAllUsers() {
        firebaseService.deleteAllUsers();
    }

    @SneakyThrows
    public SignInInternalResponse login(SignInInternalRequest request) {
        validator.validate(request);

        return firebaseService.signIn(request);
    }
}
