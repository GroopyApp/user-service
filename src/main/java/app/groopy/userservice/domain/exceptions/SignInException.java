package app.groopy.userservice.domain.exceptions;

import app.groopy.userservice.domain.models.SignInRequestDto;

public class SignInException extends Throwable {
    public SignInException(SignInRequestDto request, String error) {
        super(String.format("An error occurred trying to login: {%s}. \n exception: {%s}", request, error));
    }
}
