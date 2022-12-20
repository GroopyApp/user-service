package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.SignUpValidator;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;
import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.services.ElasticsearchUserService;
import app.groopy.userservice.infrastructure.services.FirebaseService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private SignUpValidator validator;
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
}
