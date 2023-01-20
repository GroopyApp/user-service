package app.groopy.userservice.application;

import app.groopy.userservice.application.validators.AuthenticationValidator;
import app.groopy.userservice.infrastructure.providers.AuthenticationProvider;
import app.groopy.userservice.infrastructure.providers.ElasticsearchProvider;

public abstract class AuthenticationService<I, O> {

    protected final AuthenticationValidator validator;
    protected final AuthenticationProvider authenticationProvider;
    protected final ElasticsearchProvider elasticsearchProvider;

    protected AuthenticationService(AuthenticationValidator validator, AuthenticationProvider authenticationProvider, ElasticsearchProvider elasticsearchProvider) {
        this.validator = validator;
        this.authenticationProvider = authenticationProvider;
        this.elasticsearchProvider = elasticsearchProvider;
    }

    public abstract O perform(I inputRequest);


    //FIXME DEV USE ONLY, REMOVE IT
    public void deleteAllUsers() {
        authenticationProvider.deleteAllUsers();
    }
}
