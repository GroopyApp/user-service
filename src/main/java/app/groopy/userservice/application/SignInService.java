package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.SignInValidator;
import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.domain.models.SignInInternalResponse;
import app.groopy.userservice.domain.models.common.UserDetails;
import app.groopy.userservice.infrastructure.elasticsearch.repository.ElasticsearchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignInService {

    @Autowired
    private SignInValidator validator;

    @Autowired
    private ElasticsearchUserRepository repository;

    public SignInInternalResponse login(SignInInternalRequest request) {
        //TODO remove this mock once the service will be ready to provide authentication, this should also pass by a relational DB before fetching info from elasticsearch
        UserDetails user = repository.findByUserId(request.getCredential());
        if (user == null) {
            user = UserDetails.builder()
                    .userId(request.getCredential())
                    .build();
            repository.save(user);
        }
        return SignInInternalResponse.builder()
                .user(user)
                .build();
    }
}
