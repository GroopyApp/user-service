package app.groopy.userservice.infrastructure.services;

import app.groopy.userservice.domain.models.SignInInternalRequest;
import app.groopy.userservice.domain.models.SignInInternalResponse;
import app.groopy.userservice.domain.models.SignUpInternalRequest;
import app.groopy.userservice.domain.models.SignUpInternalResponse;

public interface AuthServiceProvider {

    SignInInternalResponse signIn(SignInInternalRequest request);

    SignUpInternalResponse signUp(SignUpInternalRequest request);

    void deleteUser(String email);

    void deleteAllUsers();
}
