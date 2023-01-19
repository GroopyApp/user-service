package app.groopy.userservice.infrastructure.services;

import app.groopy.userservice.domain.models.SignInRequestDto;
import app.groopy.userservice.domain.models.SignInResponseDto;
import app.groopy.userservice.domain.models.SignUpRequestDto;
import app.groopy.userservice.domain.models.SignUpResponseDto;

public interface AuthServiceProvider {

    SignInResponseDto signIn(SignInRequestDto request);

    SignUpResponseDto signUp(SignUpRequestDto request);

    void deleteUser(String email);

    void deleteAllUsers();
}
