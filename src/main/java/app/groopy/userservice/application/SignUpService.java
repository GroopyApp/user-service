package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.SignUpValidator;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;
import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider;
import app.groopy.userservice.infrastructure.providers.FirebaseProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private SignUpValidator validator;

    @Autowired
    private FirebaseProvider firebaseProvider;
    @Autowired
    private ElasticsearchProvider elasticsearchProvider;

    public SignUpInternalResponse register(SignUpInternalRequest request) {
        validator.validate(request);

        SignUpInternalResponse response = firebaseProvider.signUp(request);

        elasticsearchProvider.save(UserDetails.builder()
                        .userId(response.getUser().getUserId())
                        .email(response.getUser().getEmail())
                .build());

        return response;
    }

    public void deleteAllUsers() {
        firebaseProvider.deleteAllUsers();
    }
}
