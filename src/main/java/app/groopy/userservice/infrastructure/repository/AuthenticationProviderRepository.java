package app.groopy.userservice.infrastructure.repository;

import app.groopy.userservice.infrastructure.models.AuthenticationSignInRequest;
import app.groopy.userservice.infrastructure.models.AuthenticationSignInResponse;
import app.groopy.userservice.infrastructure.models.AuthenticationSignUpRequest;
import app.groopy.userservice.infrastructure.models.AuthenticationSignUpResponse;

public interface AuthenticationProviderRepository {
    AuthenticationSignInResponse signIn(AuthenticationSignInRequest request);
    AuthenticationSignUpResponse signUp(AuthenticationSignUpRequest request);
    AuthenticationSignInResponse oauth(String token, String provider);
}
