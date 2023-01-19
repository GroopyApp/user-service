package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider;
import app.groopy.userservice.infrastructure.services.AuthServiceProvider;

public abstract class AuthenticationService<I, O> {

    protected final AuthenticationValidator validator;
    protected final AuthServiceProvider authServiceProvider;
    protected final ElasticsearchProvider elasticsearchProvider;


    protected AuthenticationService(AuthenticationValidator validator, AuthServiceProvider authServiceProvider, ElasticsearchProvider elasticsearchProvider) {
        this.validator = validator;
        this.authServiceProvider = authServiceProvider;
        this.elasticsearchProvider = elasticsearchProvider;
    }

    public abstract O perform(I i);


    //FIXME DEV USE ONLY, REMOVE IT
    public void deleteAllUsers() {
        authServiceProvider.deleteAllUsers();
    }
}
