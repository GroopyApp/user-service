package app.groopy.userservice.infrastructure.providers;

import app.groopy.userservice.domain.models.SignInRequestDto;
import app.groopy.userservice.domain.models.SignInResponseDto;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.domain.models.SignUpResponseDto;

public interface AuthenticationProvider {

    SignInResponseDto signIn(SignInRequestDto request);

    SignUpResponseDto signUp(SignUpRequestDto request);

    void deleteUser(String email);

    void deleteAllUsers();
}
