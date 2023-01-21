package app.groopy.userservice.application;

import app.groopy.userservice.application.mapper.ApplicationMapper;
import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.infrastructure.AuthenticationInfrastructureService;
import app.groopy.userservice.infrastructure.ElasticsearchInfrastructureService;

public abstract class AuthenticationService<I, O> {

    protected final AuthenticationValidator validator;
    protected final ApplicationMapper mapper;
    protected final AuthenticationInfrastructureService authenticationInfrastructureService;
    protected final ElasticsearchInfrastructureService elasticsearchInfrastructureService;

    protected AuthenticationService(AuthenticationValidator validator,
                                    ApplicationMapper mapper,
                                    AuthenticationInfrastructureService authenticationInfrastructureService,
                                    ElasticsearchInfrastructureService elasticsearchInfrastructureService) {
        this.validator = validator;
        this.mapper = mapper;
        this.authenticationInfrastructureService = authenticationInfrastructureService;
        this.elasticsearchInfrastructureService = elasticsearchInfrastructureService;
    }

    public abstract O perform(I inputRequest);


    //FIXME DEV USE ONLY, REMOVE IT
    public void deleteAllUsers() {
        authenticationInfrastructureService.deleteAllUsers();
    }
}
